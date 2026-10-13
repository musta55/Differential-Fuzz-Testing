package fuzz.auto.recipes;

import java.lang.reflect.Method;

import com.code_intelligence.jazzer.api.FuzzedDataProvider;

/**
 * jclouds HTTP messages, made the way jclouds makes them: through their builders. The constructors
 * reject what the engine passed them, so every bindToRequest(R extends HttpRequest, ...), error
 * handler and request filter could not be built. Reflection only: this folder is compiled for
 * every project.
 */
final class JcloudsRecipes
{
  private JcloudsRecipes() {}

  private static final String[] METHODS = {"GET", "PUT", "POST", "DELETE", "HEAD"};

  /** HttpRequest: a fuzzed method and path on a local endpoint nothing listens on. */
  static final class Request implements Recipe
  {
    @Override
    public Object build(FuzzedDataProvider data, Class<?> type) throws Exception
    {
      String path = data.consumeAsciiString(8).replaceAll("[^A-Za-z0-9]", "");
      Object builder = type.getMethod("builder").invoke(null);
      call(builder, "method", String.class, METHODS[data.consumeInt(0, METHODS.length - 1)]);
      call(builder, "endpoint", String.class, "http://localhost:1/" + path);
      return call(builder, "build", null, null);
    }
  }

  /** HttpResponse: a fuzzed status code and a fixed message. */
  static final class Response implements Recipe
  {
    @Override
    public Object build(FuzzedDataProvider data, Class<?> type) throws Exception
    {
      Object builder = type.getMethod("builder").invoke(null);
      call(builder, "statusCode", int.class, data.consumeInt(100, 599));
      call(builder, "message", String.class, "status");
      return call(builder, "build", null, null);
    }
  }

  private static Class<?> cls(Class<?> type, String name) throws ClassNotFoundException
  {
    return Class.forName(name, true, type.getClassLoader());
  }

  /** A short alphanumeric name, never empty. */
  private static String name(FuzzedDataProvider data)
  {
    return "n" + data.consumeAsciiString(8).replaceAll("[^A-Za-z0-9]", "");
  }

  /**
   * AtmosObject, for AtmosObjectName.apply(Object): the object name comes from the content
   * metadata, or when that has none, from the system metadata.
   */
  static final class AtmosObject implements Recipe
  {
    @Override
    public Object build(FuzzedDataProvider data, Class<?> type) throws Exception
    {
      Class<?> cmClass = cls(type, "org.jclouds.atmos.domain.internal.DelegatingMutableContentMetadata");
      Object content = cmClass.getConstructor().newInstance();
      if (data.consumeBoolean()) {
        cmClass.getMethod("setName", String.class).invoke(content, name(data));
      }
      Class<?> fileType = cls(type, "org.jclouds.atmos.domain.FileType");
      java.util.Date t = new java.util.Date(0);
      Object system = cls(type, "org.jclouds.atmos.domain.SystemMetadata")
          .getConstructor(byte[].class, java.util.Date.class, java.util.Date.class, String.class,
              java.util.Date.class, java.util.Date.class, int.class, String.class, String.class,
              String.class, long.class, fileType, String.class)
          .newInstance(null, t, t, "gid", t, t, 1, "id", data.consumeBoolean() ? name(data) : null,
              "default", 0L, fileType.getEnumConstants()[1], "uid");
      Class<?> impl = cls(type, "org.jclouds.atmos.domain.internal.AtmosObjectImpl");
      Class<?> userMetadata = cls(type, "org.jclouds.atmos.domain.UserMetadata");
      return impl.getConstructor(cls(type, "org.jclouds.atmos.domain.MutableContentMetadata"),
          system.getClass(), userMetadata).newInstance(content, system,
          userMetadata.getConstructor().newInstance());
    }
  }

  /**
   * S3Object, for BindS3ObjectMetadataToRequest.bindToRequest(R, Object): a key and a byte payload
   * (which carries its content length), as an upload has.
   */
  static final class S3Object implements Recipe
  {
    @Override
    public Object build(FuzzedDataProvider data, Class<?> type) throws Exception
    {
      Class<?> mdClass = cls(type, "org.jclouds.s3.domain.internal.MutableObjectMetadataImpl");
      Object metadata = mdClass.getConstructor().newInstance();
      mdClass.getMethod("setKey", String.class).invoke(metadata, name(data));
      Class<?> storage = cls(type, "org.jclouds.s3.domain.ObjectMetadata$StorageClass");
      Object[] classes = storage.getEnumConstants();
      mdClass.getMethod("setStorageClass", storage).invoke(metadata, classes[data.consumeInt(0, classes.length - 1)]);
      Class<?> impl = cls(type, "org.jclouds.s3.domain.internal.S3ObjectImpl");
      Object object = impl.getConstructor(cls(type, "org.jclouds.s3.domain.MutableObjectMetadata"))
          .newInstance(metadata);
      impl.getMethod("setPayload", byte[].class).invoke(object, (Object) data.consumeBytes(32));
      return object;
    }
  }

  /** ContentRange, for BindContentRangeToHeaders.bindToRequest(R, Object): from <= to. */
  static final class ContentRange implements Recipe
  {
    @Override
    public Object build(FuzzedDataProvider data, Class<?> type) throws Exception
    {
      long from = data.consumeLong(0, 1L << 30);
      return cls(type, "org.jclouds.glacier.util.ContentRange").getMethod("build", long.class, long.class)
          .invoke(null, from, from + data.consumeLong(0, 1L << 20));
    }
  }

  /** Iterable of names, for AlarmNamesBinder.bindToRequest(R, Object): zero to five alarm names. */
  static final class Names implements Recipe
  {
    @Override
    public Object build(FuzzedDataProvider data, Class<?> type) throws Exception
    {
      java.util.List<String> names = new java.util.ArrayList<>();
      int n = data.consumeInt(0, 5);
      for (int i = 0; i < n; i++) {
        names.add(name(data));
      }
      return names;
    }
  }

  /** Calls a public builder method; the builder's own class is private, hence setAccessible. */
  private static Object call(Object target, String name, Class<?> type, Object arg) throws Exception
  {
    Method m = type == null ? target.getClass().getMethod(name)
        : target.getClass().getMethod(name, type);
    m.setAccessible(true);
    return type == null ? m.invoke(target) : m.invoke(target, arg);
  }
}
