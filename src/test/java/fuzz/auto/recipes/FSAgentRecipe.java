package fuzz.auto.recipes;

import com.code_intelligence.jazzer.api.FuzzedDataProvider;

/**
 * FSAgent(FileSystem fs): left to the engine, fs was an uninitialised HarFileSystem or null, so
 * every call threw NullPointerException on its first line. This gives it Hadoop's local file
 * system, without checksums (a file written by a test would otherwise need a matching .crc file).
 */
final class FSAgentRecipe implements Recipe
{
  @Override
  public Object build(FuzzedDataProvider data, Class<?> type) throws Exception
  {
    ClassLoader loader = type.getClassLoader();
    Class<?> confType = Class.forName("org.apache.hadoop.conf.Configuration", true, loader);
    Class<?> fsType = Class.forName("org.apache.hadoop.fs.FileSystem", true, loader);

    Object conf = confType.getConstructor().newInstance();
    Object localFs = fsType.getMethod("getLocal", confType).invoke(null, conf);
    Object rawFs = localFs.getClass().getMethod("getRaw").invoke(localFs);
    return type.getConstructor(fsType).newInstance(rawFs);
  }
}
