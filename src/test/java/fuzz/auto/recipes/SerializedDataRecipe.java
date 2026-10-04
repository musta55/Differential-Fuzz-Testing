package fuzz.auto.recipes;

import com.code_intelligence.jazzer.api.FuzzedDataProvider;

/** Builds a valid SerializedData slice instead of fuzzing unrelated offset and size values. */
final class SerializedDataRecipe implements Recipe
{
  @Override
  public Object build(FuzzedDataProvider data, Class<?> type) throws Exception
  {
    byte[] array = new byte[data.consumeInt(1, 64)];
    for (int i = 0; i < array.length; i++) {
      array[i] = data.consumeByte();
    }
    return type.getConstructor(byte[].class, int.class, int.class)
        .newInstance(array, 0, array.length);
  }
}