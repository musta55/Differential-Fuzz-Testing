package fuzz.auto.recipes;

import java.net.URL;
import java.net.URLClassLoader;

import com.code_intelligence.jazzer.api.FuzzedDataProvider;

/**
 * java.lang.ClassLoader: abstract, and the engine only looks for project subtypes, so it found
 * none (ConfigProviderImpl.releaseConfig, ClassDefiner.defineClassClassLoader). An empty class
 * loader that finds nothing of its own is enough for code that only uses it as a key or a target.
 */
final class ClassLoaderRecipe implements Recipe
{
  @Override
  public Object build(FuzzedDataProvider data, Class<?> type)
  {
    return new URLClassLoader(new URL[0], ClassLoaderRecipe.class.getClassLoader());
  }
}
