package fuzz.auto.recipes;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

import com.code_intelligence.jazzer.api.FuzzedDataProvider;

/**
 * java.lang.reflect.Type: real Type objects only come from reflection, so the engine could not
 * build one (TypeDiscoverer.resolveTypeParameters). This picks one of each kind: a plain class,
 * a generic type, a generic array, a wildcard and a type variable.
 */
final class TypeRecipe implements Recipe
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
