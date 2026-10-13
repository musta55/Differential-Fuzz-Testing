package fuzz.auto.recipes;

import com.code_intelligence.jazzer.api.FuzzedDataProvider;
import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;
import java.net.InetSocketAddress;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import javax.security.auth.callback.LanguageCallback;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.TextOutputCallback;
import javax.security.sasl.RealmCallback;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.xml.parsers.DocumentBuilderFactory;
import org.ietf.jgss.Oid;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Recipes keyed by JDK or shared-library types (javax.*, java.*, hadoop, quartz, CDI ...).
 * They apply to every project whose code takes one of these types.
 */
final class CommonRecipes
{
  private CommonRecipes() {}

  /**
   * Builds a standard JAAS callback for the Callback interface, choosing the kind from the fuzz
   * bytes so every branch of a callback handler is reached (DefaultCallbackHandler.processCallback
   * handles Name, Password, Realm and TextOutput callbacks and rejects everything else). Always
   * building a NameCallback left four of its five branches untested.
   */
  static final class CallbackRecipe implements Recipe
  {
    @Override
    public Object build(FuzzedDataProvider data, Class<?> type)
    {
      switch (data.consumeInt(0, 4)) {
        case 0:
          return new NameCallback(prompt(data));
        case 1:
          return new PasswordCallback(prompt(data), data.consumeBoolean());
        case 2:
          return new RealmCallback(prompt(data));
        case 3:
          // The constructor accepts only INFORMATION(0), WARNING(1) and ERROR(2).
          return new TextOutputCallback(data.consumeInt(0, 2), prompt(data));
        default:
          return new LanguageCallback(); // a kind handlers usually reject: the "unsupported" path
      }
    }

    /**
     * The prompt is an argument of the callback's constructor, not of the method under test, and
     * the JDK constructors reject a null or empty one with IllegalArgumentException. Passing it
     * through would only make the recipe fail and the input be wasted, never test the handler.
     */
    private static String prompt(FuzzedDataProvider data)
    {
      String s = data.consumeAsciiString(32);
      return s.isEmpty() ? "x" : s;
    }
  }

  /**
   * Builds a harmless Runnable. Left to autofuzz, a Runnable could be any implementation on the
   * class path; for NameableThreadFactory.newThread it picked StramLocalCluster, which started a
   * Jetty server that waited for a password on the console until the run was killed.
   */
  static final class RunnableRecipe implements Recipe
  {
    @Override
    public Object build(FuzzedDataProvider data, Class<?> type)
    {
      return new Task(data.consumeInt());
    }

    /** Does nothing when run; the id gives two built tasks comparable content. */
    static final class Task implements Runnable
    {
      final int id;

      Task(int id)
      {
        this.id = id;
      }

      @Override
      public void run()
      {
      }
    }
  }

  /**
   * Built from a random IP, an InetSocketAddress makes getHostName() do a reverse DNS lookup, which
   * can take seconds; RecoverableRpcProxy.toConnectURI hit Jazzer's per-input time limit that way
   * and the whole run was killed. An unresolved address never touches DNS: getHostName() just
   * returns the stored name.
   */
  static final class InetSocketAddressRecipe implements Recipe
  {
    @Override
    public Object build(FuzzedDataProvider data, Class<?> type)
    {
      return InetSocketAddress.createUnresolved("host" + data.consumeInt(0, 99),
          data.consumeInt(0, 65535));
    }
  }

  /**
   * DataInput: the engine used to build an empty DataInputStream, so the first read threw
   * EOFException (AbstractWritableAdapter.readFields never got past readInt()). Most of the time
   * this returns a well-formed [length][serialized Map] record, the format readFields expects;
   * otherwise raw fuzz bytes, for the malformed-input path.
   */
  static final class DataInputRecipe implements Recipe
  {
    @Override
    public Object build(FuzzedDataProvider data, Class<?> type) throws Exception
    {
      ByteArrayOutputStream record = new ByteArrayOutputStream();
      // Raw bytes on 3, not 0: once the input is used up every value reads as 0, and the
      // well-formed record must be what an exhausted input gets.
      if (data.consumeInt(0, 3) == 3) {
        record.write(data.consumeBytes(64));
      } else {
        HashMap<String, Object> map = new HashMap<>();
        int n = data.consumeInt(0, 3);
        for (int i = 0; i < n; i++) {
          map.put(data.consumeAsciiString(8), data.consumeInt());
        }
        ByteArrayOutputStream serialized = new ByteArrayOutputStream();
        try (ObjectOutputStream out = new ObjectOutputStream(serialized)) {
          out.writeObject(map);
        }
        DataOutputStream out = new DataOutputStream(record);
        out.writeInt(serialized.size());
        out.write(serialized.toByteArray());
      }
      return new DataInputStream(new ByteArrayInputStream(record.toByteArray()));
    }
  }

  /**
   * ByteBuffer: abstract, with no public constructor, so every method taking one (systemds' HDF5
   * message readers, PSRpcCall.deserialize) was skipped as untestable. Wraps fuzzed bytes, the way
   * those readers receive a buffer read from a file or socket.
   */
  static final class ByteBufferRecipe implements Recipe
  {
    @Override
    public Object build(FuzzedDataProvider data, Class<?> type) throws Exception
    {
      return ByteBuffer.wrap(data.consumeBytes(64));
    }
  }

  /**
   * AWT/Swing argument types. Component, JComponent and JTextComponent are abstract, Graphics has no
   * public constructor, and KeyEvent/FocusEvent throw on a null source, so every jmeter GUI helper
   * taking one (HorizontalPanel.add, ComponentUtil, TextComponentUI.installUndo, Graph.drawSample,
   * NumberFieldErrorListener.focusLost) was skipped. Plain lightweight components work headless.
   */
  static final class SwingRecipes
  {
    private SwingRecipes() {}

    /** A JPanel, JLabel, JTextField or JTextArea; only the text ones for JTextComponent. */
    static final class Component implements Recipe
    {
      @Override
      public Object build(FuzzedDataProvider data, Class<?> type)
      {
        int k = data.consumeInt(0, 3);
        if (type.getName().equals("javax.swing.text.JTextComponent")) {
          k = 2 + k % 2;
        }
        String text = data.consumeString(20);
        switch (k) {
          case 0: return new JPanel();
          case 1: return new JLabel(text);
          case 2: return new JTextField(text);
          default: return new JTextArea(text);
        }
      }
    }

    /** Drawing onto a small in-memory image. */
    static final class Graphics implements Recipe
    {
      @Override
      public Object build(FuzzedDataProvider data, Class<?> type)
      {
        int w = data.consumeInt(1, 64);
        int h = data.consumeInt(1, 64);
        return new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB).createGraphics();
      }
    }

    /** A key event on a text field (when = 0, so both sides get the same event). */
    static final class Key implements Recipe
    {
      @Override
      public Object build(FuzzedDataProvider data, Class<?> type)
      {
        int id = data.pickValue(new int[] {KeyEvent.KEY_PRESSED, KeyEvent.KEY_RELEASED, KeyEvent.KEY_TYPED});
        JTextField source = new JTextField(data.consumeString(20));
        if (id == KeyEvent.KEY_TYPED) {
          return new KeyEvent(source, id, 0L, 0, KeyEvent.VK_UNDEFINED, data.consumeChar());
        }
        return new KeyEvent(source, id, 0L, 0, data.consumeInt(0, 255), KeyEvent.CHAR_UNDEFINED);
      }
    }

    /** Focus gained or lost on a text field. */
    static final class Focus implements Recipe
    {
      @Override
      public Object build(FuzzedDataProvider data, Class<?> type)
      {
        int id = data.consumeBoolean() ? FocusEvent.FOCUS_GAINED : FocusEvent.FOCUS_LOST;
        return new FocusEvent(new JTextField(data.consumeString(20)), id);
      }
    }
  }

  /**
   * org.w3c.dom Node/Document are interfaces whose implementations live inside the JDK's parser, so
   * methods walking a DOM (XMLDefaultMutableTreeNode.initNode, TemplateManager.parseTemplateNode,
   * AnchorModifier.addAnchorUrls) were skipped. Builds a small document: a root element with a few
   * child elements, each with one attribute and some text. Tag and attribute names come from a fixed
   * list (fuzzed names are rarely valid XML names), including the ones those methods look for.
   */
  static final class XmlRecipe implements Recipe
  {
    private static final String[] NAMES = {
        "root", "html", "a", "form", "frame", "iframe", "input", "template", "name", "description",
        "fileName", "parameters", "parameter", "key", "value", "href", "src", "action", "type"};

    @Override
    public Object build(FuzzedDataProvider data, Class<?> type) throws Exception
    {
      Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
      Element root = doc.createElement(data.pickValue(NAMES));
      doc.appendChild(root);
      List<Object> nodes = new ArrayList<>();
      nodes.add(doc);
      nodes.add(root);
      int n = data.consumeInt(0, 4);
      for (int i = 0; i < n; i++) {
        Element e = doc.createElement(data.pickValue(NAMES));
        e.setAttribute(data.pickValue(NAMES), data.consumeString(20));
        e.appendChild(doc.createTextNode(data.consumeString(20)));
        root.appendChild(e);
        nodes.add(e);
        nodes.add(e.getAttributes().item(0));
        nodes.add(e.getFirstChild());
      }
      if (type.getName().equals("org.w3c.dom.Document")) {
        return doc;
      }
      return nodes.get(data.consumeInt(0, nodes.size() - 1)); // the document, an element, attribute or text
    }
  }

  /**
   * java.net.URL: autofuzz feeds new URL(String) random text, which is almost never a valid URL, so
   * methods taking one (RegexpHTMLParser, URLCollection.addURL, HC4CookieHandler) never ran. Builds
   * an http(s) URL with a fuzzed path and query. The host is always local: URL.equals/hashCode
   * resolve the host, and an unknown name means a slow DNS lookup on every call.
   */
  static final class UrlRecipe implements Recipe
  {
    @Override
    public Object build(FuzzedDataProvider data, Class<?> type) throws Exception
    {
      String protocol = data.consumeBoolean() ? "http" : "https";
      String host = data.consumeBoolean() ? "localhost" : "127.0.0.1";
      int port = data.pickValue(new int[] {-1, 80, 443, 8080});
      String file = "/" + data.consumeString(30);
      return new URL(protocol, host, port, file);
    }
  }

  /**
   * org.ietf.jgss.Oid only accepts a dotted object identifier, which random text never is, so
   * DelegatingKerberosScheme/DelegatingSPNegoScheme.generateGSSToken never ran. Picks one of the
   * real GSS mechanism ids those schemes use.
   */
  static final class OidRecipe implements Recipe
  {
    private static final String[] OIDS = {
        "1.2.840.113554.1.2.2",   // Kerberos v5
        "1.3.6.1.5.5.2",          // SPNEGO
        "1.2.840.113554.1.2.2.1", // Kerberos principal name
        "1.2.840.113554.1.2.1.1"};  // user name

    @Override
    public Object build(FuzzedDataProvider data, Class<?> type) throws Exception
    {
      return new Oid(data.pickValue(OIDS));
    }
  }

  /**
   * XStream Mapper: every jmeter converter (ScriptWrapperConverter, HashTreeConverter,
   * HTTPSamplerBaseConverter ...) takes one in its constructor, and the interface's implementations
   * are a chain of wrappers nothing generic can assemble, so those converters never ran. Uses the
   * mapper a real XStream instance builds, which is where jmeter's converters get theirs from.
   * Reached through reflection, so it is the XStream loaded by the side being tested.
   */
  static final class XStreamMapperRecipe implements Recipe
  {
    @Override
    public Object build(FuzzedDataProvider data, Class<?> type) throws Exception
    {
      Class<?> xstream = Class.forName("com.thoughtworks.xstream.XStream", true, type.getClassLoader());
      Object x = xstream.getConstructor().newInstance();
      return xstream.getMethod("getMapper").invoke(x);
    }
  }

  /**
   * Caffeine Cache: autofuzz built it as a view over an async cache, which removes a failed entry on
   * a background thread. Whether that had happened by the time of the comparison was down to timing,
   * so jmeter's message renderers (BinaryMessageRenderer.getValueFromFile ...) were reported as
   * nondeterministic. Builds the cache the way jmeter does (PublisherSampler.buildCache:
   * Caffeine.newBuilder().build()), with a same-thread executor so cleanup happens before the call
   * returns. Reached through reflection, so it is the Caffeine loaded by the side being tested.
   */
  static final class CaffeineCacheRecipe implements Recipe
  {
    @Override
    public Object build(FuzzedDataProvider data, Class<?> type) throws Exception
    {
      Class<?> caffeine = Class.forName("com.github.benmanes.caffeine.cache.Caffeine", true,
          type.getClassLoader());
      Object builder = caffeine.getMethod("newBuilder").invoke(null);
      Executor sameThread = new Executor() { // runs each task on the calling thread
        @Override
        public void execute(Runnable task) {
          task.run();
        }
      };
      builder = caffeine.getMethod("executor", Executor.class).invoke(builder, sameThread);
      return caffeine.getMethod("build").invoke(builder);
    }
  }

  /**
   * java.lang.reflect.Type: real Type objects only come from reflection, so the engine could not
   * build one (TypeDiscoverer.resolveTypeParameters). This picks one of each kind: a plain class,
   * a generic type, a generic array, a wildcard and a type variable.
   */
  static final class TypeRecipe implements Recipe
  {
    /** Its fields only exist to read their generic types. */
    @SuppressWarnings("unused")
    private static final class Holder<T>
    {
      List<String> list;
      Map<String, Integer> map;
      List<String>[] array;
      List<? extends Number> wildcard;
      T variable;
    }

    @Override
    public Object build(FuzzedDataProvider data, Class<?> type) throws Exception
    {
      switch (data.consumeInt(0, 7)) {
        case 0:
          return String.class;
        case 1:
          return Integer.class;
        case 2:
          return int.class;
        case 3:
          return field("list");
        case 4:
          return field("map");
        case 5:
          return field("array");
        case 6:
          return ((ParameterizedType) field("wildcard")).getActualTypeArguments()[0];
        default:
          return field("variable");
      }
    }

    private static Type field(String name) throws NoSuchFieldException
    {
      return Holder.class.getDeclaredField(name).getGenericType();
    }
  }

  /**
   * Comparator: it used to be mapped to java.text.Collator, which is abstract itself, so nothing
   * could be built (StablePriorityQueue(int, Comparator)). These are fixed comparators, the same
   * objects on both sides. Written for Java 7 (apex-core compiles with -source 1.7), so no
   * Comparator.naturalOrder().
   */
  static final class ComparatorRecipe implements Recipe
  {
    private static final Comparator<Object> NATURAL = new NaturalOrder();

    @Override
    public Object build(FuzzedDataProvider data, Class<?> type)
    {
      switch (data.consumeInt(0, 2)) {
        case 0:
          return NATURAL;
        case 1:
          return Collections.reverseOrder();
        default:
          return String.CASE_INSENSITIVE_ORDER;
      }
    }

    /** Natural ordering: what Comparator.naturalOrder() returns from Java 8 on. */
    private static final class NaturalOrder implements Comparator<Object>, Serializable
    {
      @Override
      @SuppressWarnings("unchecked")
      public int compare(Object a, Object b)
      {
        return ((Comparable<Object>) a).compareTo(b);
      }
    }
  }

  /**
   * java.lang.annotation.Annotation: annotation instances only come from reading an annotated
   * element, so the engine could not build one (AnnotationUtils.getQualifierHashCode). These are read
   * from the fields below: no members, every kind of member at its default, and every kind filled.
   */
  static final class AnnotationRecipe implements Recipe
  {
    @Retention(RetentionPolicy.RUNTIME)
    @interface Marker
    {
    }

    @Retention(RetentionPolicy.RUNTIME)
    @interface Sample
    {
      int number() default 0;
      String text() default "";
      int[] ints() default {};
      long[] longs() default {};
      short[] shorts() default {};
      double[] doubles() default {};
      float[] floats() default {};
      boolean[] booleans() default {};
      byte[] bytes() default {};
      char[] chars() default {};
      String[] texts() default {};
    }

    @Marker
    private static final Object MARKER = null;

    @Sample
    private static final Object DEFAULTS = null;

    @Sample(number = 7, text = "a", ints = {1, 2}, longs = {3L}, shorts = {4}, doubles = {0.5},
        floats = {1.5f}, booleans = {true}, bytes = {5}, chars = {'x'}, texts = {"b", "c"})
    private static final Object FILLED = null;

    /** FILLED first: a used-up input reads as 0 and gets the annotation that reaches every branch. */
    private static final String[] FIELDS = {"FILLED", "DEFAULTS", "MARKER"};

    @Override
    public Object build(FuzzedDataProvider data, Class<?> type) throws Exception
    {
      String field = FIELDS[data.consumeInt(0, FIELDS.length - 1)];
      return AnnotationRecipe.class.getDeclaredField(field).getAnnotations()[0];
    }
  }

  /**
   * java.lang.ClassLoader: abstract, and the engine only looks for project subtypes, so it found
   * none (ConfigProviderImpl.releaseConfig, ClassDefiner.defineClassClassLoader). An empty class
   * loader that finds nothing of its own is enough for code that only uses it as a key or a target.
   */
  static final class ClassLoaderRecipe implements Recipe
  {
    @Override
    public Object build(FuzzedDataProvider data, Class<?> type)
    {
      return new URLClassLoader(new URL[0], ClassLoaderRecipe.class.getClassLoader());
    }
  }

  /**
   * java.lang.reflect.Method and Constructor: Autofuzz built them through their internal
   * constructors with fuzzed values, including the internal slot that points at the method inside
   * the JVM. Such a forged object crashed the whole JVM (SIGSEGV in Executable.getParameters0) when
   * ParameterUtil.getName asked it for its parameters. These are real members of harmless JDK
   * classes, the same objects on both sides; calling any of them cannot exit or harm the JVM.
   */
  static final class MemberRecipes
  {
    private MemberRecipes() {}

    private static final Class<?>[] OWNERS = {Object.class, String.class, ArrayList.class};

    /** A real public method of Object, String or ArrayList. */
    static final class Method implements Recipe
    {
      @Override
      public Object build(FuzzedDataProvider data, Class<?> type)
      {
        java.lang.reflect.Method[] methods = owner(data).getMethods();
        return methods[data.consumeInt(0, methods.length - 1)];
      }
    }

    /** A real public constructor of Object, String or ArrayList. */
    static final class Constructor implements Recipe
    {
      @Override
      public Object build(FuzzedDataProvider data, Class<?> type)
      {
        java.lang.reflect.Constructor<?>[] constructors = owner(data).getConstructors();
        return constructors[data.consumeInt(0, constructors.length - 1)];
      }
    }

    private static Class<?> owner(FuzzedDataProvider data)
    {
      return OWNERS[data.consumeInt(0, OWNERS.length - 1)];
    }
  }

  /**
   * Stand-ins for the Servlet API interfaces of Filter.doFilter(request, response, chain), which the
   * engine cannot build (their real implementations live inside a web server). The response and the
   * chain record what the filter did with them, and the engine compares arguments after the call,
   * so "sent 401" and "passed the request on" become comparable. Written with reflection because
   * this folder is compiled for projects that do not have the Servlet API.
   */
  static final class ServletRecipes
  {
    private ServletRecipes() {}

    /** An HTTP request with a fuzzed remote address, path and cookies. */
    static final class Request implements Recipe
    {
      @Override
      public Object build(FuzzedDataProvider data, Class<?> type) throws Exception
      {
        final String remoteAddr = data.consumeBoolean() ? "127.0.0.1" : "10.0.0." + data.consumeInt(1, 9);
        final String uri = data.consumeBoolean() ? "/ws" : "/ws/v2/" + data.consumeInt(0, 9);
        final Object cookies = cookies(data, type.getClassLoader());
        return proxy(type, "javax.servlet.http.HttpServletRequest", new Handler()
        {
          @Override
          Object call(String name, Object[] args)
          {
            if (name.equals("getRemoteAddr") || name.equals("getLocalAddr")) {
              return name.equals("getRemoteAddr") ? remoteAddr : "127.0.0.1";
            }
            if (name.equals("getRequestURI")) {
              return uri;
            }
            if (name.equals("getCookies")) {
              return cookies;
            }
            return null;
          }

          @Override
          public String toString()
          {
            return "Request(" + remoteAddr + " " + uri + ")";
          }
        });
      }

      /** No cookies, or some of: the proxy's user cookie, the client token cookie, another one. */
      private static Object cookies(FuzzedDataProvider data, ClassLoader loader) throws Exception
      {
        if (data.consumeInt(0, 3) == 0) {
          return null;
        }
        Class<?> cookieType = Class.forName("javax.servlet.http.Cookie", true, loader);
        String[] names = {"proxy-user", "dt-client", "other"};
        List<Object> list = new ArrayList<>();
        for (String name : names) {
          if (data.consumeBoolean()) {
            String value = "v" + data.consumeInt(0, 9);
            list.add(cookieType.getConstructor(String.class, String.class).newInstance(name, value));
          }
        }
        Object array = Array.newInstance(cookieType, list.size());
        for (int i = 0; i < list.size(); i++) {
          Array.set(array, i, list.get(i));
        }
        return array;
      }
    }

    /** An HTTP response that records the errors sent and the names of the cookies added. */
    static final class Response implements Recipe
    {
      @Override
      public Object build(FuzzedDataProvider data, Class<?> type) throws Exception
      {
        return proxy(type, "javax.servlet.http.HttpServletResponse", new Handler()
        {
          final List<Integer> errors = new ArrayList<>();
          final List<String> cookieNames = new ArrayList<>(); // not values: tokens are random

          @Override
          Object call(String name, Object[] args) throws Exception
          {
            if (name.equals("sendError")) {
              errors.add((Integer) args[0]);
            } else if (name.equals("addCookie")) {
              cookieNames.add((String) args[0].getClass().getMethod("getName").invoke(args[0]));
            }
            return null;
          }

          @Override
          public String toString()
          {
            return "Response(errors=" + errors + ", cookies=" + cookieNames + ")";
          }
        });
      }
    }

    /** A filter chain that records whether the request was passed on, and as which user. */
    static final class Chain implements Recipe
    {
      @Override
      public Object build(FuzzedDataProvider data, Class<?> type) throws Exception
      {
        return proxy(type, null, new Handler()
        {
          boolean called;
          String user;

          @Override
          Object call(String name, Object[] args) throws Exception
          {
            if (name.equals("doFilter")) {
              called = true;
              Object principal = args[0].getClass().getMethod("getUserPrincipal").invoke(args[0]);
              user = principal == null ? null : principal.toString();
            }
            return null;
          }

          @Override
          public String toString()
          {
            return "Chain(called=" + called + ", user=" + user + ")";
          }
        });
      }
    }

    /** A ServletRequestEvent: a do-nothing ServletContext and a request from {@link Request}. */
    static final class RequestEvent implements Recipe
    {
      @Override
      public Object build(FuzzedDataProvider data, Class<?> type) throws Exception
      {
        ClassLoader loader = type.getClassLoader();
        Class<?> contextType = Class.forName("javax.servlet.ServletContext", true, loader);
        Class<?> requestType = Class.forName("javax.servlet.ServletRequest", true, loader);
        Object context = proxy(contextType, null, new Handler()
        {
          @Override
          Object call(String name, Object[] args)
          {
            return null;
          }
        });
        Object request = new Request().build(data, requestType);
        return type.getConstructor(contextType, requestType).newInstance(context, request);
      }
    }

    /** Answers equals/hashCode/toString itself, everything else through call(). */
    abstract static class Handler implements InvocationHandler
    {
      abstract Object call(String name, Object[] args) throws Exception;

      @Override
      public Object invoke(Object proxy, Method m, Object[] args) throws Exception
      {
        String name = m.getName();
        if (name.equals("equals")) {
          return proxy == args[0];
        }
        if (name.equals("hashCode")) {
          return System.identityHashCode(proxy);
        }
        if (name.equals("toString")) {
          return toString();
        }
        Object result = call(name, args);
        if (result == null && m.getReturnType().isPrimitive()) {
          return defaultValue(m.getReturnType());
        }
        return result;
      }
    }

    private static Object defaultValue(Class<?> t)
    {
      if (t == boolean.class) {
        return false;
      }
      if (t == void.class) {
        return null;
      }
      if (t == long.class) {
        return 0L;
      }
      return 0; // int, and the rarely used short/char/byte/float/double return no value here
    }

    /** A proxy implementing {@code type}, plus the more specific HTTP interface when given. */
    private static Object proxy(Class<?> type, String httpType, Handler handler) throws Exception
    {
      ClassLoader loader = type.getClassLoader();
      Class<?>[] interfaces = httpType == null
          ? new Class<?>[] {type}
          : new Class<?>[] {Class.forName(httpType, true, loader)};
      return Proxy.newProxyInstance(loader, interfaces, handler);
    }
  }

  /**
   * One of a few fixed values, for a parameter whose valid inputs random data almost never hits:
   * an instruction text a parser reads by position, a JSON text, a column number.
   */
  static final class OneOf implements Recipe
  {
    private final Object[] values;

    OneOf(Object... values)
    {
      this.values = values;
    }

    @Override
    public Object build(FuzzedDataProvider data, Class<?> type)
    {
      return data.pickValue(values);
    }
  }

  /**
   * A stand-in for a callback parameter (Consumer, BiConsumer, a predicate): a proxy that records
   * every call made to it, so the two sides' records are compared after the call (same items, same
   * order). A boolean method answers from a few fuzzed booleans, so a predicate's "stop" is
   * reachable; any other method with a result answers a small number 0..3 (a Function<T, Integer>
   * picking a partition). It implements all the named interfaces, for overloads sharing one key.
   */
  static final class Recorder implements Recipe
  {
    private final String[] interfaces;

    Recorder(String... interfaces)
    {
      this.interfaces = interfaces;
    }

    @Override
    public Object build(FuzzedDataProvider data, Class<?> type) throws Exception
    {
      Class<?>[] types = new Class<?>[interfaces.length];
      for (int i = 0; i < types.length; i++) {
        types[i] = Class.forName(interfaces[i], false, type.getClassLoader());
      }
      boolean[] answers = new boolean[8];
      int[] numbers = new int[8];
      for (int i = 0; i < answers.length; i++) {
        answers[i] = data.consumeBoolean();
        numbers[i] = data.consumeInt(0, 3);
      }
      return java.lang.reflect.Proxy.newProxyInstance(type.getClassLoader(), types, new Calls(answers, numbers));
    }

    /** What the proxy was called with: method name and arguments, in call order. */
    static final class Calls implements java.lang.reflect.InvocationHandler
    {
      private final List<Object> calls = new ArrayList<>();
      private final boolean[] answers;
      private final int[] numbers;
      private int next;

      Calls(boolean[] answers, int[] numbers)
      {
        this.answers = answers;
        this.numbers = numbers;
      }

      @Override
      public Object invoke(Object proxy, java.lang.reflect.Method m, Object[] args)
      {
        if (m.getDeclaringClass() == Object.class) {
          switch (m.getName()) {
            case "equals": return proxy == args[0];
            case "hashCode": return 0;
            default: return "recorder";
          }
        }
        calls.add(m.getName());
        calls.add(args == null ? new ArrayList<Object>() : java.util.Arrays.asList(args));
        int i = next++ % answers.length;
        if (m.getReturnType() == boolean.class) {
          return answers[i];
        }
        return m.getReturnType() == void.class ? null : (Object) numbers[i];
      }
    }
  }

  /**
   * A scratch folder for one side: "fuzzside-original" or "fuzzside-refactored" in one temp folder.
   * Recipes that hand out files and folders put them here, so what one side writes cannot change
   * what the other reads. It is emptied the first time it is asked for while a side's input is being
   * built, so every call (and the self-check's rebuild) starts from the same empty folder; Digest
   * ignores the two names when comparing text.
   */
  static final class SideFolder
  {
    private static File root;
    private static final Map<String, Object> EMPTIED_FOR = new HashMap<>(); // side -> input it was emptied for

    static synchronized File of(FuzzedDataProvider data, Class<?> type) throws Exception
    {
      if (root == null) {
        root = Files.createTempDirectory("fuzzpath").toFile();
      }
      String side = side(type.getClassLoader());
      File dir = new File(root, "fuzzside-" + side);
      if (EMPTIED_FOR.get(side) != data) {
        delete(dir);
        dir.mkdirs();
        EMPTIED_FOR.put(side, data);
      }
      return dir;
    }

    /** The side a class was loaded for (fuzz.auto.SideLoader.side), or "shared". */
    private static String side(ClassLoader loader)
    {
      for (Class<?> c = loader == null ? null : loader.getClass(); c != null; c = c.getSuperclass()) {
        try {
          java.lang.reflect.Field f = c.getDeclaredField("side");
          f.setAccessible(true);
          return String.valueOf(f.get(loader));
        } catch (Exception e) {
          // not this class
        }
      }
      return "shared";
    }

    private static void delete(File f)
    {
      File[] children = f.listFiles();
      if (children != null) {
        for (File child : children) {
          delete(child);
        }
      }
      f.setWritable(true);
      f.delete();
    }
  }

  /**
   * Hadoop Path: a random path string points to no file, so file-reading methods only ever hit
   * "file not found". This writes the fuzzed bytes to a file in the side's own folder
   * ({@link SideFolder}) and returns its path; now and then it returns a path that does not exist,
   * to keep that case too.
   */
  static final class PathRecipe implements Recipe
  {
    @Override
    public Object build(FuzzedDataProvider data, Class<?> type) throws Exception
    {
      File file = new File(SideFolder.of(data, type), "f" + data.consumeInt(0, 9));
      // Always start from a fresh file. A previous input may have made it unwritable
      // (FSUtil.setPermission); writing to it then failed, the engine fell back to a relative path,
      // and that changed the permissions of the test's working directory instead.
      file.delete();
      if (data.consumeInt(0, 7) != 0) { // otherwise: a missing file
        try (FileOutputStream out = new FileOutputStream(file)) {
          out.write(data.consumeBytes(64));
        }
      }
      return type.getConstructor(String.class).newInstance(file.getAbsolutePath());
    }
  }

  /**
   * Hadoop FileSystem is abstract; the engine picked HarFileSystem and never initialised it, so
   * every call threw NullPointerException on its first use (FSUtil.setPermission). This returns
   * Hadoop's local file system, without checksums (files written by PathRecipe have no .crc file).
   */
  static final class FileSystemRecipe implements Recipe
  {
    @Override
    public Object build(FuzzedDataProvider data, Class<?> type) throws Exception
    {
      Class<?> confType = Class.forName("org.apache.hadoop.conf.Configuration", true,
          type.getClassLoader());
      Object conf = confType.getConstructor().newInstance();
      Object localFs = type.getMethod("getLocal", confType).invoke(null, conf);
      return localFs.getClass().getMethod("getRaw").invoke(localFs);
    }
  }

  /**
   * CDI types the engine could not build, which normally come from a CDI container. Written with
   * reflection because this folder is also compiled for projects without CDI.
   */
  static final class CdiRecipes
  {
    private CdiRecipes() {}

    /**
     * AnnotatedType: a real one, made by deltaspike's own AnnotatedTypeBuilder from a plain class or
     * from DefaultEchoService, whose @Named("DefaultEchoService") is what
     * NamingConventionAwareMetadataFilter rewrites. Only classes with few methods: an AnnotatedType
     * keeps its methods in a HashSet, and the comparison looks at the first 32 elements of a set, so
     * for String (100+ methods) each side showed a different 32 and reported a false divergence.
     */
    static final class AnnotatedType implements Recipe
    {
      private static final String[] CLASSES = {
          "org.apache.deltaspike.example.echo.DefaultEchoService", "java.lang.Object", "java.lang.Number"};

      @Override
      public Object build(FuzzedDataProvider data, Class<?> type) throws Exception
      {
        ClassLoader loader = type.getClassLoader();
        Class<?> source;
        try {
          source = Class.forName(CLASSES[data.consumeInt(0, CLASSES.length - 1)], false, loader);
        } catch (ClassNotFoundException e) {
          source = Object.class; // the example module is not in this jar
        }
        Class<?> builderType = Class.forName(
            "org.apache.deltaspike.core.util.metadata.builder.AnnotatedTypeBuilder", true, loader);
        Object builder = builderType.getConstructor().newInstance();
        builderType.getMethod("readFromType", Class.class).invoke(builder, source);
        return builderType.getMethod("create").invoke(builder);
      }
    }

    /** BeforeBeanDiscovery: a container event; a stand-in whose methods do nothing. */
    static final class BeforeBeanDiscovery implements Recipe
    {
      @Override
      public Object build(FuzzedDataProvider data, Class<?> type)
      {
        return Proxy.newProxyInstance(type.getClassLoader(), new Class<?>[] {type},
            new ServletRecipes.Handler()
            {
              @Override
              Object call(String name, Object[] args)
              {
                return null;
              }
            });
      }
    }

    /**
     * ProcessAnnotatedType: a container event carrying an AnnotatedType. The stand-in hands out one
     * from the recipe above and records whether the observer replaced it or vetoed it.
     */
    static final class ProcessAnnotatedType implements Recipe
    {
      @Override
      public Object build(FuzzedDataProvider data, Class<?> type) throws Exception
      {
        Class<?> annotatedTypeType =
            Class.forName("javax.enterprise.inject.spi.AnnotatedType", true, type.getClassLoader());
        final Object annotatedType = new AnnotatedType().build(data, annotatedTypeType);
        return Proxy.newProxyInstance(type.getClassLoader(), new Class<?>[] {type},
            new ServletRecipes.Handler()
            {
              boolean replaced;
              boolean vetoed;

              @Override
              Object call(String name, Object[] args)
              {
                if (name.equals("getAnnotatedType")) {
                  return annotatedType;
                }
                if (name.equals("setAnnotatedType")) {
                  replaced = true;
                } else if (name.equals("veto")) {
                  vetoed = true;
                }
                return null;
              }

              @Override
              public String toString()
              {
                return "ProcessAnnotatedType(replaced=" + replaced + ", vetoed=" + vetoed + ")";
              }
            });
      }
    }
  }

  /**
   * Quartz objects for job factories (deltaspike's CdiAwareJobFactory.newJob). Reflection only:
   * this folder is compiled for every project.
   */
  static final class QuartzRecipes
  {
    private QuartzRecipes() {}

    /**
     * TriggerFiredBundle: a fired trigger for a job of class SimpleSchedulerJob1 (a deltaspike
     * example job with a public no-argument constructor). Quartz's default job factory creates the
     * job from that class and reads the trigger's job data, so the trigger is a real SimpleTriggerImpl.
     */
    static final class Bundle implements Recipe
    {
      @Override
      public Object build(FuzzedDataProvider data, Class<?> type) throws Exception
      {
        ClassLoader loader = type.getClassLoader();
        Class<?> jobClass = Class.forName("org.apache.deltaspike.example.scheduler.SimpleSchedulerJob1",
            true, loader);
        Class<?> builderType = Class.forName("org.quartz.JobBuilder", true, loader);
        Object builder = builderType.getMethod("newJob", Class.class).invoke(null, jobClass);
        Object jobDetail = builderType.getMethod("build").invoke(builder);
        Object trigger = Class.forName("org.quartz.impl.triggers.SimpleTriggerImpl", true, loader)
            .getConstructor().newInstance();

        // (JobDetail, OperableTrigger, Calendar, boolean recovering, Date x4)
        Constructor<?> ctor = type.getConstructors()[0];
        Object[] args = new Object[ctor.getParameterCount()];
        args[0] = jobDetail;
        args[1] = trigger;
        args[3] = false;
        return ctor.newInstance(args);
      }
    }

    /** Scheduler: an interface; the stand-in only answers getContext() with a real SchedulerContext. */
    static final class Scheduler implements Recipe
    {
      @Override
      public Object build(FuzzedDataProvider data, Class<?> type) throws Exception
      {
        final Object context = Class.forName("org.quartz.SchedulerContext", true, type.getClassLoader())
            .getConstructor().newInstance();
        return Proxy.newProxyInstance(type.getClassLoader(), new Class<?>[] {type},
            new ServletRecipes.Handler()
            {
              @Override
              Object call(String name, Object[] args)
              {
                return name.equals("getContext") ? context : null;
              }

              @Override
              public String toString()
              {
                return "Scheduler";
              }
            });
      }
    }
  }

  /**
   * CDI Bean: normally made by a CDI container, so the engine could not build one
   * (ImmutableBeanWrapper's constructor). That constructor only asks it for getBeanClass() and
   * getInjectionPoints(), so this stand-in answers those: Object.class and no injection points.
   * Written with reflection because this folder is also compiled for projects without CDI.
   */
  static final class BeanRecipe implements Recipe
  {
    @Override
    public Object build(FuzzedDataProvider data, Class<?> type)
    {
      return Proxy.newProxyInstance(type.getClassLoader(), new Class<?>[] {type},
          new ServletRecipes.Handler()
          {
            @Override
            Object call(String name, Object[] args)
            {
              if (name.equals("getBeanClass")) {
                return Object.class;
              }
              if (name.equals("getInjectionPoints")) {
                return Collections.emptySet();
              }
              return null;
            }

            @Override
            public String toString()
            {
              return "Bean(Object)";
            }
          });
    }
  }
}
