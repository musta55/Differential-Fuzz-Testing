package fuzz.auto.recipes;

import com.code_intelligence.jazzer.api.FuzzedDataProvider;
import java.io.File;
import java.io.FileOutputStream;

/**
 * openmeetings recipes, keyed by openmeetings' own classes.
 */
final class OpenmeetingsRecipes
{
  private OpenmeetingsRecipes() {}

  /**
   * JSON text with some of the keys a fromString(String) reads, each holding a short text or a
   * small number. The methods start with new JSONObject(s), which every random string failed, so
   * their bodies never ran. Optionally wrapped in the root key the converter unwraps first.
   */
  static final class JsonText implements Recipe
  {
    private final String root;
    private final String[] keys;

    JsonText(String root, String... keys)
    {
      this.root = root;
      this.keys = keys;
    }

    @Override
    public Object build(FuzzedDataProvider data, Class<?> type)
    {
      StringBuilder json = new StringBuilder("{");
      for (String key : keys) {
        if (data.consumeBoolean()) {
          if (json.length() > 1) {
            json.append(',');
          }
          json.append(quote(key)).append(':');
          json.append(data.consumeBoolean() ? quote(data.consumeString(10))
              : String.valueOf(data.consumeInt(-5, 5)));
        }
      }
      json.append('}');
      if (root != null && data.consumeBoolean()) {
        return "{" + quote(root) + ":" + json + "}";
      }
      return json.toString();
    }

    /** A JSON string literal. */
    private static String quote(String s)
    {
      StringBuilder out = new StringBuilder("\"");
      for (char c : s.toCharArray()) {
        if (c == '"' || c == '\\') {
          out.append('\\').append(c);
        } else if (c < 0x20) {
          out.append(String.format("\\u%04x", (int) c));
        } else {
          out.append(c);
        }
      }
      return out.append('"').toString();
    }
  }

  /**
   * An existing directory for CleanupUnit and its subclasses: the side's own scratch folder, with
   * 0..3 small files in it. The constructor rejects anything but an existing directory, and the
   * no-argument constructor uses ".", whose files cleanup() then deletes.
   */
  static final class CleanupFolder implements Recipe
  {
    @Override
    public Object build(FuzzedDataProvider data, Class<?> type) throws Exception
    {
      File dir = CommonRecipes.SideFolder.of(data, type);
      int files = data.consumeInt(0, 3);
      for (int i = 0; i < files; i++) {
        FileOutputStream out = new FileOutputStream(new File(dir, "file" + i));
        try {
          out.write(data.consumeBytes(16));
        } finally {
          out.close();
        }
      }
      return dir;
    }
  }

  /** A CleanupUnit over a {@link CleanupFolder}, so cleanup() only deletes its own files. */
  static final class CleanupUnit implements Recipe
  {
    @Override
    public Object build(FuzzedDataProvider data, Class<?> type) throws Exception
    {
      return type.getConstructor(File.class).newInstance(new CleanupFolder().build(data, type));
    }
  }

  /**
   * Command line options as the admin tool builds them: one required group of 1..3 OmOptions
   * (short name, order and help text from small lists), and sometimes one optional extra option.
   * printUsage reads the first required group, which the built Options never had.
   */
  static final class HelpOptions implements Recipe
  {
    @Override
    public Object build(FuzzedDataProvider data, Class<?> type) throws Exception
    {
      ClassLoader loader = type.getClassLoader();
      Class<?> optionsClass = Class.forName("org.apache.commons.cli.Options", true, loader);
      Class<?> optionClass = Class.forName("org.apache.commons.cli.Option", true, loader);
      Class<?> groupClass = Class.forName("org.apache.commons.cli.OptionGroup", true, loader);
      java.lang.reflect.Constructor<?> omOption = Class.forName("org.apache.openmeetings.cli.OmOption", true, loader)
          .getConstructor(String.class, int.class, String.class, String.class, boolean.class, String.class);
      Object options = optionsClass.getConstructor().newInstance();
      Object group = groupClass.getConstructor().newInstance();
      String[] names = {"h", "b", "r", "i", "f"};
      int n = data.consumeInt(1, 3);
      for (int i = 0; i < n; i++) {
        String name = names[i];
        groupClass.getMethod("addOption", optionClass).invoke(group, omOption.newInstance(name,
            data.consumeInt(0, 3), name, name + "-long", data.consumeBoolean(), data.pickValue(new String[] {"", "help"})));
      }
      groupClass.getMethod("setRequired", boolean.class).invoke(group, true);
      optionsClass.getMethod("addOptionGroup", groupClass).invoke(options, group);
      if (data.consumeBoolean()) {
        optionsClass.getMethod("addOption", optionClass).invoke(options,
            omOption.newInstance(null, 0, "v", "verbose", false, "verbose"));
      }
      return options;
    }
  }

  /**
   * A WebDAV REPORT response: status 207 (multistatus) or an error status, with a multistatus XML
   * body holding 0..2 responses and sometimes a sync token, or a body that is not XML. Built by
   * autofuzz, the response had no status line, so every call failed reading it.
   */
  static final class DavResponse implements Recipe
  {
    @Override
    public Object build(FuzzedDataProvider data, Class<?> type) throws Exception
    {
      ClassLoader loader = type.getClassLoader();
      StringBuilder xml = new StringBuilder("<?xml version=\"1.0\"?><D:multistatus xmlns:D=\"DAV:\">");
      for (int n = data.consumeInt(0, 2); n > 0; n--) {
        xml.append("<D:response><D:href>/cal/").append(n).append(".ics</D:href><D:propstat><D:prop><D:getetag>\"")
            .append(data.consumeInt(0, 9)).append("\"</D:getetag></D:prop><D:status>HTTP/1.1 200 OK</D:status>")
            .append("</D:propstat></D:response>");
      }
      if (data.consumeBoolean()) {
        xml.append("<D:sync-token>http://x/sync/").append(data.consumeInt(0, 9)).append("</D:sync-token>");
      }
      xml.append("</D:multistatus>");
      String body = data.consumeInt(0, 4) == 0 ? "not xml" : xml.toString();
      int status = data.pickValue(new Integer[] {207, 207, 200, 404, 500});
      Class<?> versionClass = Class.forName("org.apache.http.ProtocolVersion", true, loader);
      Object version = Class.forName("org.apache.http.HttpVersion", true, loader).getField("HTTP_1_1").get(null);
      Object response = Class.forName("org.apache.http.message.BasicHttpResponse", true, loader)
          .getConstructor(versionClass, int.class, String.class).newInstance(version, status, "status");
      Class<?> entityClass = Class.forName("org.apache.http.HttpEntity", true, loader);
      Object entity = Class.forName("org.apache.http.entity.StringEntity", true, loader)
          .getConstructor(String.class, String.class).newInstance(body, "UTF-8");
      response.getClass().getMethod("setEntity", entityClass).invoke(response, entity);
      return response;
    }
  }

  /**
   * A SyncMethod for a fixed calendar URL with an empty report (no properties, sync token none or
   * "1", depth none, 0 or 1). Built by autofuzz, the two sides' constructors took different numbers
   * of input bytes (the refactored one delegates to a private constructor), so the arguments built
   * after it differed between the sides.
   */
  static final class SyncMethod implements Recipe
  {
    @Override
    public Object build(FuzzedDataProvider data, Class<?> type) throws Exception
    {
      Class<?> report = Class.forName("org.apache.openmeetings.service.calendar.caldav.methods.SyncReportInfo",
          true, type.getClassLoader());
      Class<?> names = Class.forName("org.apache.jackrabbit.webdav.property.DavPropertyNameSet", true,
          type.getClassLoader());
      Object info = report.getConstructor(String.class, names, int.class, int.class).newInstance(
          data.consumeBoolean() ? "1" : null, names.getConstructor().newInstance(), 1, data.consumeInt(-1, 1));
      return type.getConstructor(java.net.URI.class, report).newInstance(java.net.URI.create("http://cal/x/"), info);
    }
  }

  /**
   * A backup FileTree as the importer builds it: a root with 0..4 file items added through add(),
   * each with id 1..4 and a parent that is none or an earlier item. Built by autofuzz, the tree was
   * always an empty root, so process() had nothing to visit.
   */
  static final class FileTree implements Recipe
  {
    @Override
    public Object build(FuzzedDataProvider data, Class<?> type) throws Exception
    {
      ClassLoader loader = type.getClassLoader();
      Class<?> base = Class.forName("org.apache.openmeetings.db.entity.file.BaseFileItem", true, loader);
      Class<?> fileItem = Class.forName("org.apache.openmeetings.db.entity.file.FileItem", true, loader);
      java.lang.reflect.Constructor<?> make = type.getDeclaredConstructor();
      make.setAccessible(true);
      Object tree = make.newInstance();
      java.lang.reflect.Method add = type.getDeclaredMethod("add", base);
      add.setAccessible(true);
      for (long id = 1, n = data.consumeInt(0, 4); id <= n; id++) {
        Object item = fileItem.getConstructor().newInstance();
        base.getMethod("setId", Long.class).invoke(item, id);
        long parent = data.consumeLong(0, id - 1);
        base.getMethod("setParentId", Long.class).invoke(item, parent == 0 ? null : parent);
        base.getMethod("setName", String.class).invoke(item, "f" + id);
        add.invoke(tree, item);
      }
      return tree;
    }
  }
}
