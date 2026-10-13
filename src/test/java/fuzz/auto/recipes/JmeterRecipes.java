package fuzz.auto.recipes;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.code_intelligence.jazzer.api.FuzzedDataProvider;

/**
 * jmeter test elements. Reflection only: this folder is compiled for every project.
 */
final class JmeterRecipes
{
  private JmeterRecipes() {}

  /**
   * The JSON renderers (JMESPath, JSONPath): their GUI fields are only created by init(), which
   * jmeter always calls first, so process() failed reading a null expression field. init(), then
   * an expression from a small list in the expression field.
   */
  static final class JsonRenderer implements Recipe
  {
    private final String[] expressions;

    JsonRenderer(String... expressions)
    {
      this.expressions = expressions;
    }

    @Override
    public Object build(FuzzedDataProvider data, Class<?> type) throws Exception
    {
      Object renderer = type.getConstructor().newInstance();
      type.getMethod("init").invoke(renderer);
      Method field = Class.forName("org.apache.jmeter.extractor.json.render.AbstractRenderAsJsonRenderer",
          true, type.getClassLoader()).getDeclaredMethod("getExpressionField");
      field.setAccessible(true);
      Object textField = field.invoke(renderer);
      textField.getClass().getMethod("setText", String.class).invoke(textField, data.pickValue(expressions));
      return renderer;
    }
  }

  /**
   * One test element class, for a GUI's configure / modifyTestElement(TestElement): these cast the
   * element to the one type the GUI edits, so any other TestElement failed the cast. The element is
   * made with its no-arg constructor and its simple setters are called with fuzzed values, in name
   * order so both sides set the same values.
   */
  static final class Element implements Recipe
  {
    private final String className;

    Element(String className)
    {
      this.className = className;
    }

    @Override
    public Object build(FuzzedDataProvider data, Class<?> type) throws Exception
    {
      Class<?> c = Class.forName(className, true, type.getClassLoader());
      Object element = c.getConstructor().newInstance();
      List<Method> setters = new ArrayList<>();
      for (Method m : c.getMethods()) {
        if (m.getName().startsWith("set") && m.getParameterCount() == 1 && simple(m.getParameterTypes()[0])) {
          setters.add(m);
        }
      }
      Collections.sort(setters, new Comparator<Method>() {
        @Override
        public int compare(Method a, Method b) {
          return a.toString().compareTo(b.toString());
        }
      });
      for (Method m : setters) {
        try {
          m.invoke(element, value(data, m.getParameterTypes()[0]));
        } catch (Exception e) {
          // a setter that rejects its value leaves the default; the same on both sides
        }
      }
      return element;
    }

    private static boolean simple(Class<?> t)
    {
      return t == String.class || t == int.class || t == long.class || t == boolean.class;
    }

    private static Object value(FuzzedDataProvider data, Class<?> t)
    {
      if (t == int.class) {
        return data.consumeInt(-5, 100);
      }
      if (t == long.class) {
        return data.consumeLong(-5, 100);
      }
      if (t == boolean.class) {
        return data.consumeBoolean();
      }
      return data.consumeString(12);
    }
  }

  /**
   * A jmeter function (__char, __property, __StringToFile, ...). jmeter calls setParameters(...)
   * before execute(...); built without it, its values were null and execute failed on its first
   * line. 0..4 parameters (another count when the function rejects that one), each a word from a
   * small list or a fuzzed string, and a fresh variables map for this thread, holding "name".
   */
  static final class Function implements Recipe
  {
    private static final String[] WORDS =
        {"name", "65", "3.5", "true", "UTF-8", "SHA-256", "yyyy-MM-dd", "2024-01-31", "x,y", ""};

    /** Whether the first parameter is a file the function writes (StringToFile). */
    private final boolean firstIsFile;

    /** The class to make, for an abstract function type; null to make the type itself. */
    private final String concrete;

    Function(boolean firstIsFile)
    {
      this(firstIsFile, null);
    }

    /**
     * For an abstract function, one of its subclasses: jmeter's side holds only the changed
     * classes, so the engine cannot find an unchanged subclass by itself.
     */
    Function(boolean firstIsFile, String concrete)
    {
      this.firstIsFile = firstIsFile;
      this.concrete = concrete;
    }

    @Override
    public Object build(FuzzedDataProvider data, Class<?> type) throws Exception
    {
      ClassLoader loader = type.getClassLoader();
      Object vars = Class.forName("org.apache.jmeter.threads.JMeterVariables", true, loader)
          .getConstructor().newInstance();
      vars.getClass().getMethod("put", String.class, String.class).invoke(vars, "name", data.pickValue(WORDS));
      Object context = Class.forName("org.apache.jmeter.threads.JMeterContextService", true, loader)
          .getMethod("getContext").invoke(null);
      context.getClass().getMethod("setVariables", vars.getClass()).invoke(context, vars);

      Class<?> made = concrete == null ? type : Class.forName(concrete, true, loader);
      Object function = made.getConstructor().newInstance();
      java.lang.reflect.Constructor<?> variable =
          Class.forName("org.apache.jmeter.engine.util.CompoundVariable", true, loader).getConstructor(String.class);
      Method setParameters = made.getMethod("setParameters", java.util.Collection.class);
      int first = data.consumeInt(0, 4);
      Exception rejected = null;
      for (int k = 0; k <= 4; k++) {
        List<Object> parameters = new ArrayList<>();
        for (int i = 0; i < (first + k) % 5; i++) {
          String value = firstIsFile && i == 0
              ? new java.io.File(CommonRecipes.SideFolder.of(data, type), "out.txt").getPath()
              : data.consumeBoolean() ? data.pickValue(WORDS) : data.consumeString(8);
          parameters.add(variable.newInstance(value));
        }
        try {
          setParameters.invoke(function, parameters);
          return function;
        } catch (java.lang.reflect.InvocationTargetException e) {
          rejected = e; // wrong number of parameters for this function: try the next count
        }
      }
      throw rejected;
    }
  }

  /**
   * A report consumer as the report generator sets it up: named, with a SampleContext (stopConsuming()
   * stores its result there, and failed on the missing one) and, for a SampleWriterConsumer, an
   * output file in the side's own folder.
   */
  static final class Consumer implements Recipe
  {
    @Override
    public Object build(FuzzedDataProvider data, Class<?> type) throws Exception
    {
      ClassLoader loader = type.getClassLoader();
      Object consumer = type.getConstructor().newInstance();
      // the default name ends in the identity hash code, a new number each run
      type.getMethod("setName", String.class).invoke(consumer, type.getSimpleName());
      Class<?> context = Class.forName("org.apache.jmeter.report.processor.SampleContext", true, loader);
      type.getMethod("setSampleContext", context).invoke(consumer, context.getConstructor().newInstance());
      if (type.getSimpleName().equals("SampleWriterConsumer")) {
        java.io.File out = new java.io.File(CommonRecipes.SideFolder.of(data, type), data.pickValue(
            new String[] {"out.csv", "out", "out.tar.gz", ".csv"}));
        type.getMethod("setOutputFile", java.io.File.class).invoke(consumer, out);
      }
      return consumer;
    }
  }

  /**
   * A report Sample whose data row matches its columns (the usual CSV result columns), with
   * plausible values. A built Sample had columns but an empty row, so reading any field failed.
   */
  static final class Sample implements Recipe
  {
    private static final String[] COLUMNS = {"timeStamp", "elapsed", "label", "responseCode",
        "responseMessage", "threadName", "dataType", "success", "failureMessage", "bytes", "sentBytes",
        "grpThreads", "allThreads", "URL", "Latency", "IdleTime", "Connect"};

    @Override
    public Object build(FuzzedDataProvider data, Class<?> type) throws Exception
    {
      ClassLoader loader = type.getClassLoader();
      Class<?> metadataClass = Class.forName("org.apache.jmeter.report.core.SampleMetadata", true, loader);
      Object metadata = metadataClass.getConstructor(char.class, String[].class)
          .newInstance(',', (Object) COLUMNS);
      String[] row = {
          String.valueOf(data.consumeLong(0, 2000000000000L)), String.valueOf(data.consumeInt(0, 5000)),
          data.pickValue(new String[] {"home", "login", "search"}), data.pickValue(new String[] {"200", "404", "500", ""}),
          data.consumeString(8), "thread 1-1", "text", String.valueOf(data.consumeBoolean()), data.consumeString(8),
          String.valueOf(data.consumeInt(0, 9999)), String.valueOf(data.consumeInt(0, 9999)), "1", "1",
          "http://localhost/", String.valueOf(data.consumeInt(0, 5000)), "0", String.valueOf(data.consumeInt(0, 5000))};
      return Class.forName("org.apache.jmeter.report.core.Sample", true, loader)
          .getConstructor(long.class, metadataClass, String[].class)
          .newInstance(data.consumeLong(0, 1000), metadata, (Object) row);
    }
  }

  /**
   * A JSONPathAssertion with its JSON path and expected value set (an empty path made every
   * doAssert fail before its first line) and its flags fuzzed.
   */
  static final class JsonPathAssertion implements Recipe
  {
    @Override
    public Object build(FuzzedDataProvider data, Class<?> type) throws Exception
    {
      Object assertion = type.getConstructor().newInstance();
      type.getMethod("setJsonPath", String.class)
          .invoke(assertion, data.pickValue(new String[] {"$", "$.a", "$.a[0]", "$..a", "$.b"}));
      type.getMethod("setExpectedValue", String.class)
          .invoke(assertion, data.pickValue(new String[] {"1", "x", "[1,2]", "null", ".*", ""}));
      for (String flag : new String[] {"setJsonValidationBool", "setExpectNull", "setInvert", "setIsRegex"}) {
        type.getMethod(flag, boolean.class).invoke(assertion, data.consumeBoolean());
      }
      return assertion;
    }
  }

  /** A PowerTableModel with 1..3 columns (a built one had none, so removeColumn always failed). */
  static final class TableModel implements Recipe
  {
    @Override
    public Object build(FuzzedDataProvider data, Class<?> type) throws Exception
    {
      int n = data.consumeInt(1, 3);
      String[] headers = new String[n];
      Class<?>[] classes = new Class<?>[n];
      for (int i = 0; i < n; i++) {
        headers[i] = "column" + i;
        classes[i] = data.pickValue(new Class<?>[] {String.class, Integer.class, Boolean.class});
      }
      return type.getConstructor(String[].class, Class[].class).newInstance(headers, classes);
    }
  }

  /**
   * For the abstract AbstractJavaSamplerClient: JavaSampler's own ErrorSamplerClient, the one
   * subclass that keeps its setupTest / getDefaultParameters (the test clients override both, so
   * those methods never ran). It is an inner class, made with a JavaSampler as its outer object.
   */
  static final class JavaSamplerClient implements Recipe
  {
    @Override
    public Object build(FuzzedDataProvider data, Class<?> type) throws Exception
    {
      ClassLoader loader = type.getClassLoader();
      Class<?> sampler = Class.forName("org.apache.jmeter.protocol.java.sampler.JavaSampler", true, loader);
      java.lang.reflect.Constructor<?> client =
          Class.forName("org.apache.jmeter.protocol.java.sampler.JavaSampler$ErrorSamplerClient", true, loader)
              .getDeclaredConstructor(sampler);
      client.setAccessible(true);
      return client.newInstance(sampler.getConstructor().newInstance());
    }
  }

  /**
   * RenderAsRegexp after init(), which creates its regex field (processJavaRegex read the field
   * and failed on the missing one), with one of a few regexes typed into it. All are valid:
   * Jazzer's regex sanitizer stops the run on any pattern that fails to compile.
   */
  static final class RegexpRenderer implements Recipe
  {
    @Override
    public Object build(FuzzedDataProvider data, Class<?> type) throws Exception
    {
      Object renderer = type.getConstructor().newInstance();
      type.getMethod("init").invoke(renderer);
      java.lang.reflect.Field field = type.getDeclaredField("regexpField");
      field.setAccessible(true);
      Object textField = field.get(renderer);
      textField.getClass().getMethod("setText", String.class).invoke(textField,
          data.pickValue(new String[] {"a", "(\\w+)", "a(b)?c", "[0-9]+", "", ".*"}));
      return renderer;
    }
  }

  /**
   * An InterleaveControl with 1..3 child controllers and a fuzzed style. Built empty, it had no
   * current child, so currentReturnedNull always failed removing it.
   */
  static final class Interleave implements Recipe
  {
    @Override
    public Object build(FuzzedDataProvider data, Class<?> type) throws Exception
    {
      Object control = type.getConstructor().newInstance();
      type.getMethod("setStyle", int.class).invoke(control, data.consumeInt(0, 1));
      Class<?> child = Class.forName("org.apache.jmeter.control.GenericController", true, type.getClassLoader());
      Class<?> element = Class.forName("org.apache.jmeter.testelement.TestElement", true, type.getClassLoader());
      int n = data.consumeInt(1, 3);
      for (int i = 0; i < n; i++) {
        type.getMethod("addTestElement", element).invoke(control, child.getConstructor().newInstance());
      }
      return control;
    }
  }

  /**
   * The report's TemplateVisitor copies files from a source folder to a target folder and runs
   * FreeMarker on ".fmkr" templates. Both folders are in the side's own scratch folder ("src" and
   * "out"); templates are read from "src", with x in the data.
   */
  static final class TemplateVisitor implements Recipe
  {
    @Override
    public Object build(FuzzedDataProvider data, Class<?> type) throws Exception
    {
      ClassLoader loader = type.getClassLoader();
      java.io.File src = new java.io.File(CommonRecipes.SideFolder.of(data, type), "src");
      java.io.File out = new java.io.File(CommonRecipes.SideFolder.of(data, type), "out");
      src.mkdirs();
      out.mkdirs();
      Class<?> configurationClass = Class.forName("freemarker.template.Configuration", true, loader);
      Object configuration = configurationClass
          .getConstructor(Class.forName("freemarker.template.Version", true, loader))
          .newInstance(configurationClass.getField("DEFAULT_INCOMPATIBLE_IMPROVEMENTS").get(null));
      configurationClass.getMethod("setDirectoryForTemplateLoading", java.io.File.class).invoke(configuration, src);
      Class<?> dataContext = Class.forName("org.apache.jmeter.report.core.DataContext", true, loader);
      @SuppressWarnings("unchecked")
      java.util.Map<String, Object> values = (java.util.Map<String, Object>) dataContext.getConstructor().newInstance();
      values.put("x", data.consumeString(8));
      return type.getConstructor(java.nio.file.Path.class, java.nio.file.Path.class, configurationClass, dataContext)
          .newInstance(src.toPath(), out.toPath(), configuration, values);
    }
  }

  /**
   * A path the TemplateVisitor visits, under its "src" folder: the folder itself, a sub folder, a
   * plain file, a template (valid, or one naming a missing value) or a missing file.
   */
  static final class TemplatePath implements Recipe
  {
    @Override
    public Object build(FuzzedDataProvider data, Class<?> type) throws Exception
    {
      java.io.File src = new java.io.File(CommonRecipes.SideFolder.of(data, type), "src");
      src.mkdirs();
      java.io.File file;
      switch (data.consumeInt(0, 5)) {
        case 0: return src.toPath();
        case 1: file = new java.io.File(src, "sub"); file.mkdirs(); return file.toPath();
        case 2: file = new java.io.File(src, "a.txt"); write(file, data.consumeString(16)); return file.toPath();
        case 3: file = new java.io.File(src, "t.html.fmkr"); write(file, "<p>${x}</p>"); return file.toPath();
        case 4: file = new java.io.File(src, "u.fmkr"); write(file, "${missing}"); return file.toPath();
        default: return new java.io.File(src, "none.txt").toPath();
      }
    }

    private static void write(java.io.File file, String text) throws java.io.IOException
    {
      java.io.FileOutputStream out = new java.io.FileOutputStream(file);
      try {
        out.write(text.getBytes("UTF-8"));
      } finally {
        out.close();
      }
    }
  }

  /** The attributes of the visitor's "src" folder (the visited path's own are not read). */
  static final class TemplateAttributes implements Recipe
  {
    @Override
    public Object build(FuzzedDataProvider data, Class<?> type) throws Exception
    {
      java.io.File src = new java.io.File(CommonRecipes.SideFolder.of(data, type), "src");
      src.mkdirs();
      return java.nio.file.Files.readAttributes(src.toPath(), java.nio.file.attribute.BasicFileAttributes.class);
    }
  }

  /**
   * The XStream pieces jmeter's SaveService uses to save results, so a converter's
   * marshal(obj, writer, context) runs as it does when jmeter saves a file. Built by autofuzz, the
   * writer and context were half-made objects, and every call failed with a ConversionException
   * before the converter's own code ran. What: "converter" (built from SaveService's mapper),
   * "writer" (an XML writer, inside one open node) or "context" (a marshaller over SaveService's
   * converters).
   */
  static final class SaveService implements Recipe
  {
    private final String what;

    SaveService(String what)
    {
      this.what = what;
    }

    @Override
    public Object build(FuzzedDataProvider data, Class<?> type) throws Exception
    {
      ClassLoader loader = type.getClassLoader();
      java.lang.reflect.Field saver = Class.forName("org.apache.jmeter.save.SaveService", true, loader)
          .getDeclaredField("JTLSAVER");
      saver.setAccessible(true);
      Object xstream = saver.get(null);
      Class<?> xstreamClass = Class.forName("com.thoughtworks.xstream.XStream", true, loader);
      Class<?> mapperClass = Class.forName("com.thoughtworks.xstream.mapper.Mapper", true, loader);
      Object mapper = xstreamClass.getMethod("getMapper").invoke(xstream);
      if (what.equals("converter")) {
        return type.getConstructor(mapperClass).newInstance(mapper);
      }
      Class<?> writerClass = Class.forName("com.thoughtworks.xstream.io.xml.PrettyPrintWriter", true, loader);
      Object writer = writerClass.getConstructor(java.io.Writer.class).newInstance(new java.io.StringWriter());
      writerClass.getMethod("startNode", String.class).invoke(writer, "config");
      if (what.equals("writer")) {
        return writer;
      }
      return Class.forName("com.thoughtworks.xstream.core.TreeMarshaller", true, loader)
          .getConstructor(Class.forName("com.thoughtworks.xstream.io.HierarchicalStreamWriter", true, loader),
              Class.forName("com.thoughtworks.xstream.converters.ConverterLookup", true, loader), mapperClass)
          .newInstance(writer, xstreamClass.getMethod("getConverterLookup").invoke(xstream), mapper);
    }
  }

  /**
   * FileRowColContainer reads its file in the constructor; the fuzzed paths never existed, so it
   * was never built. A file in the side's scratch folder: 0..4 lines of 1..3 cells joined by the
   * delimiter (",", ";" or tab), sometimes a whitespace-only line, which ends the data.
   */
  static final class RowColFile implements Recipe
  {
    @Override
    public Object build(FuzzedDataProvider data, Class<?> type) throws Exception
    {
      String delim = data.pickValue(new String[] {",", ";", "\t"});
      StringBuilder text = new StringBuilder();
      for (int n = data.consumeInt(0, 4); n > 0; n--) {
        if (data.consumeInt(0, 4) == 0) {
          text.append("  \n");
          continue;
        }
        for (int c = data.consumeInt(1, 3); c > 0; c--) {
          text.append(data.pickValue(new String[] {"a", "b c", "1", ""}));
          if (c > 1) {
            text.append(delim);
          }
        }
        text.append('\n');
      }
      java.io.File file = new java.io.File(CommonRecipes.SideFolder.of(data, type), "rows.csv");
      TemplatePath.write(file, text.toString());
      return type.getConstructor(String.class, String.class).newInstance(file.getPath(), delim);
    }
  }

  /**
   * A tree searcher, as the search panel makes one from its text field: plain text or a regex, case
   * sensitive or not. The texts are all valid regexes: Jazzer's regex sanitizer stops the run on
   * any pattern that fails to compile.
   */
  static final class Searcher implements Recipe
  {
    @Override
    public Object build(FuzzedDataProvider data, Class<?> type) throws Exception
    {
      String kind = data.consumeBoolean() ? "RegexpSearcher" : "RawTextSearcher";
      return Class.forName("org.apache.jmeter.gui.action." + kind, true, type.getClassLoader())
          .getConstructor(boolean.class, String.class)
          .newInstance(data.consumeBoolean(), data.pickValue(new String[] {"", "a", "alpha", "ALPHA", "[a-z]+", "x.*"}));
    }
  }

  /**
   * A text component, as Swing passes to a text UI's createUI: a text area, field or pane.
   * TextAreaUIWithUndo.createUI leaves itself registered as Swing's global "TextAreaUI" (shared by
   * both sides and every input), and a text area made after that went through it and could fail on
   * one side only. The registration is cleared first, so every input starts from Swing's own UI.
   */
  static final class TextComponent implements Recipe
  {
    @Override
    public Object build(FuzzedDataProvider data, Class<?> type) throws Exception
    {
      javax.swing.UIManager.put("TextAreaUI", null);
      switch (data.consumeInt(0, 2)) {
        case 0: return new javax.swing.JTextArea(data.consumeString(8));
        case 1: return new javax.swing.JTextField(data.consumeString(8));
        default: return new javax.swing.JTextPane();
      }
    }
  }

  /**
   * An httpclient parameters file in the side's scratch folder (or a missing one): 0..4 lines
   * "name$Type=value" with types Integer, Long, Boolean, HttpVersion, an unknown one or none, and
   * values that parse or not.
   */
  static final class HttpParamsFile implements Recipe
  {
    @Override
    public Object build(FuzzedDataProvider data, Class<?> type) throws Exception
    {
      java.io.File file = new java.io.File(CommonRecipes.SideFolder.of(data, type), "httpclient.parameters");
      if (data.consumeInt(0, 4) == 0) {
        return new java.io.File(CommonRecipes.SideFolder.of(data, type), "missing.parameters").getPath();
      }
      StringBuilder text = new StringBuilder();
      for (int n = data.consumeInt(0, 4); n > 0; n--) {
        text.append("p").append(n)
            .append(data.pickValue(new String[] {"$Integer", "$Long", "$Boolean", "$HttpVersion", "$Other", ""}))
            .append('=').append(data.pickValue(new String[] {"1", "true", "1.1", "2", "x"})).append('\n');
      }
      TemplatePath.write(file, text.toString());
      return file.getPath();
    }
  }

  /**
   * The private GenericHttpParams callback, as load(file, HttpParams) makes it: jmeter's own
   * anonymous subclass around an empty httpclient parameter set.
   */
  static final class HttpParams implements Recipe
  {
    @Override
    public Object build(FuzzedDataProvider data, Class<?> type) throws Exception
    {
      ClassLoader loader = type.getClassLoader();
      Class<?> params = Class.forName("org.apache.http.params.HttpParams", true, loader);
      java.lang.reflect.Constructor<?> callback =
          Class.forName("org.apache.jmeter.protocol.http.sampler.HttpClientDefaultParameters$1", true, loader)
              .getDeclaredConstructor(params);
      callback.setAccessible(true);
      return callback.newInstance(
          Class.forName("org.apache.http.params.BasicHttpParams", true, loader).getConstructor().newInstance());
    }
  }

  /**
   * LocalTrustStoreSSLSocketFactory reads a truststore file in its constructor; none existed, so
   * it was never built. An empty JKS truststore in the side's scratch folder.
   */
  static final class TrustStore implements Recipe
  {
    @Override
    public Object build(FuzzedDataProvider data, Class<?> type) throws Exception
    {
      java.io.File file = new java.io.File(CommonRecipes.SideFolder.of(data, type), "trust.jks");
      if (!file.exists()) {
        java.security.KeyStore store = java.security.KeyStore.getInstance("JKS");
        store.load(null, null);
        java.io.FileOutputStream out = new java.io.FileOutputStream(file);
        try {
          store.store(out, "changeit".toCharArray());
        } finally {
          out.close();
        }
      }
      return type.getConstructor(java.io.File.class).newInstance(file);
    }
  }
}
