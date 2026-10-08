package fuzz.auto.recipes;

import com.code_intelligence.jazzer.api.FuzzedDataProvider;

/**
 * Hadoop FileSystem is abstract; the engine picked HarFileSystem and never initialised it, so
 * every call threw NullPointerException on its first use (FSUtil.setPermission). This returns
 * Hadoop's local file system, without checksums (files written by PathRecipe have no .crc file).
 */
final class FileSystemRecipe implements Recipe
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
