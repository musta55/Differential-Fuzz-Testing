package fuzz.auto.recipes;

import com.code_intelligence.jazzer.api.FuzzedDataProvider;

/**
 * A hand-written way to build one class that the generic engine cannot build on its own.
 *
 * <p>{@code type} is the class as loaded by the side being tested, so a recipe must reach it
 * through reflection on {@code type}, never by naming the project class directly: a directly
 * named class would come from the wrong loader and not be an instance of {@code type}.
 */
public interface Recipe
{
  Object build(FuzzedDataProvider data, Class<?> type) throws Exception;
}
