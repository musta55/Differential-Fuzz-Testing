package fuzz.auto;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.jar.JarFile;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

/**
 * Loads one side of a project, "original" or "refactored", under the real class names.
 *
 * <p>A side is its compiled changed classes ({@code target/sides/<project>/<side>}, written by
 * {@code scripts/compile_sides.py}) in front of the project's fat jar. This loader looks in those
 * two places FIRST and asks its parent only for what they do not contain. So every project class a
 * side touches belongs to that side, including the unchanged ones that call back into a changed
 * one. The two versions of a class never meet, and neither has to be renamed.
 *
 * <p>Always shared with the parent, never loaded twice: the JDK, this engine, Jazzer and JUnit.
 * They are identical for both sides, and the engine hands JDK values (strings, arrays,
 * collections) to both.
 */
final class SideLoader extends URLClassLoader
{
  static {
    registerAsParallelCapable();
  }

  static final String ORIGINAL = "original";
  static final String REFACTORED = "refactored";

  /** Package prefixes that always come from the parent loader. */
  private static final String[] SHARED = {
      "java.", "javax.", "jdk.", "sun.", "com.sun.", "org.w3c.", "org.xml.", "org.ietf.",
      "fuzz.auto.", "com.code_intelligence.jazzer.", "org.junit.", "org.opentest4j."};

  private static final Map<String, SideLoader> LOADERS = new HashMap<>();

  final String side;
  private final List<File> locations;
  private List<String> classNames;

  /**
   * True when this project's jar has the SLF4J API but no SLF4J backend (deltaspike). Then the
   * API would come from the jar and the backend from the parent, two copies of the same SLF4J
   * types, and creating any logger failed with a LinkageError. So SLF4J is taken entirely from
   * the parent, like the JDK. A project that ships its own backend (apex-core) keeps its own.
   */
  private final boolean shareSlf4j;

  private SideLoader(String side, List<File> locations, ClassLoader parent)
  {
    super(toUrls(locations), parent);
    this.side = side;
    this.locations = locations;
    this.shareSlf4j = findResource("org/slf4j/LoggerFactory.class") != null
        && findResource("org/slf4j/impl/StaticLoggerBinder.class") == null;
  }

  /** The loader for one side of a project, created once from the manifest's "sides" block. */
  static synchronized SideLoader of(String project, String side) throws Exception
  {
    String key = project + "/" + side;
    SideLoader loader = LOADERS.get(key);
    if (loader == null) {
      JsonObject sides = GenericDifferential.manifest(project).getAsJsonObject("sides");
      if (sides == null) {
        throw new IllegalStateException("manifest of " + project
            + " has no \"sides\" — run scripts/compile_sides.py " + project);
      }
      List<File> locations = new ArrayList<>();
      locations.add(new File(sides.get(side).getAsString()));
      JsonArray classpath = sides.getAsJsonArray("classpath");
      for (int i = 0; i < classpath.size(); i++) {
        locations.add(new File(classpath.get(i).getAsString()));
      }
      loader = new SideLoader(side, locations, SideLoader.class.getClassLoader());
      LOADERS.put(key, loader);
    }
    return loader;
  }

  /**
   * Make the loader of {@code c} the current thread's context class loader, and return the
   * previous one so the caller can restore it in a {@code finally}.
   *
   * <p>Code that looks classes up through the context loader must find this side's classes, not
   * the application's: Jazzer's autofuzz finds the implementations of an abstract type that way,
   * and so do project calls such as {@code ServiceLoader.load}.
   */
  static ClassLoader useContextOf(Class<?> c)
  {
    Thread current = Thread.currentThread();
    ClassLoader previous = current.getContextClassLoader();
    current.setContextClassLoader(c.getClassLoader());
    return previous;
  }

  /** Load and initialise a class of this side. */
  Class<?> load(String className) throws ClassNotFoundException
  {
    // Static initialisers run here and may look classes up through the context loader (Hadoop's
    // UserGroupInformation does), so it must point at this side, as it does for calls.
    Thread current = Thread.currentThread();
    ClassLoader previous = current.getContextClassLoader();
    current.setContextClassLoader(this);
    try {
      return Class.forName(className, true, this);
    } finally {
      current.setContextClassLoader(previous);
    }
  }

  @Override
  protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException
  {
    synchronized (getClassLoadingLock(name)) {
      Class<?> c = findLoadedClass(name);
      if (c == null) {
        c = isShared(name) ? parentFirst(name) : ownFirst(name);
      }
      if (resolve) {
        resolveClass(c);
      }
      return c;
    }
  }

  private Class<?> ownFirst(String name) throws ClassNotFoundException
  {
    try {
      return findClass(name);
    } catch (ClassNotFoundException notOurs) {
      return getParent().loadClass(name);
    }
  }

  private Class<?> parentFirst(String name) throws ClassNotFoundException
  {
    try {
      return getParent().loadClass(name);
    } catch (ClassNotFoundException notTheParents) {
      return findClass(name); // e.g. a javax.* library that only the fat jar has
    }
  }

  @Override
  protected Class<?> findClass(String name) throws ClassNotFoundException
  {
    Class<?> c = super.findClass(name);
    // Jazzer records coverage per class NAME, so the other side's class of the same name would
    // overwrite this one's entry. Keep a copy now, while it is still ours.
    JazzerCoverage.remember(side, c);
    return c;
  }

  private boolean isShared(String name)
  {
    if (shareSlf4j && name.startsWith("org.slf4j.")) {
      return true;
    }
    for (String prefix : SHARED) {
      if (name.startsWith(prefix)) {
        return true;
      }
    }
    return false;
  }

  /**
   * Every named class this side can load itself (top-level and nested, e.g. {@code Outer$Inner}):
   * its own directory, then the fat jar.
   */
  synchronized List<String> classNames()
  {
    if (classNames == null) {
      List<String> out = new ArrayList<>();
      for (File location : locations) {
        if (location.isDirectory()) {
          collectDirectory(location, "", out);
        } else if (location.isFile()) {
          collectJar(location, out);
        }
      }
      classNames = out;
    }
    return classNames;
  }

  private static void collectDirectory(File dir, String packagePrefix, List<String> out)
  {
    File[] children = dir.listFiles();
    if (children == null) {
      return;
    }
    for (File child : children) {
      String name = child.getName();
      if (child.isDirectory()) {
        collectDirectory(child, packagePrefix + name + ".", out);
      } else if (isNamedClassFile(name)) {
        out.add(packagePrefix + name.substring(0, name.length() - ".class".length()));
      }
    }
  }

  /**
   * A class file of a top-level or nested class. Anonymous and local classes ({@code Outer$1},
   * {@code Outer$1Local}) are left out: they can never be instantiated as a subtype, so loading
   * them would be wasted work.
   */
  private static boolean isNamedClassFile(String fileName)
  {
    if (!fileName.endsWith(".class")) {
      return false;
    }
    int i = fileName.indexOf('$');
    while (i >= 0) {
      if (i + 1 < fileName.length() && Character.isDigit(fileName.charAt(i + 1))) {
        return false;
      }
      i = fileName.indexOf('$', i + 1);
    }
    return true;
  }

  private static void collectJar(File jar, List<String> out)
  {
    try (JarFile jf = new JarFile(jar)) {
      Enumeration<java.util.jar.JarEntry> entries = jf.entries();
      while (entries.hasMoreElements()) {
        String name = entries.nextElement().getName();
        if (isNamedClassFile(name) && !name.startsWith("META-INF/")) {
          out.add(name.substring(0, name.length() - ".class".length()).replace('/', '.'));
        }
      }
    } catch (IOException unreadable) {
      // an unreadable jar contributes no names; loading from it fails loudly elsewhere
    }
  }

  private static URL[] toUrls(List<File> files)
  {
    URL[] urls = new URL[files.size()];
    for (int i = 0; i < files.size(); i++) {
      try {
        urls[i] = files.get(i).toURI().toURL();
      } catch (MalformedURLException e) {
        throw new IllegalArgumentException("bad classpath entry: " + files.get(i), e);
      }
    }
    return urls;
  }
}
