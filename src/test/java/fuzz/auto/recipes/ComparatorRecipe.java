package fuzz.auto.recipes;

import java.io.Serializable;
import java.util.Collections;
import java.util.Comparator;

import com.code_intelligence.jazzer.api.FuzzedDataProvider;

/**
 * Comparator: it used to be mapped to java.text.Collator, which is abstract itself, so nothing
 * could be built (StablePriorityQueue(int, Comparator)). These are fixed comparators, the same
 * objects on both sides. Written for Java 7 (apex-core compiles with -source 1.7), so no
 * Comparator.naturalOrder().
 */
final class ComparatorRecipe implements Recipe
{
  private static final Comparator<Object> NATURAL = new NaturalOrder();

  @Override
  public Object build(FuzzedDataProvider data, Class<?> type)
  {
    switch (data.consumeInt(0, 2)) {
      case 0:
        return NATURAL;
      case 1:
        return Collections.reverseOrder();
      default:
        return String.CASE_INSENSITIVE_ORDER;
    }
  }

  /** Natural ordering: what Comparator.naturalOrder() returns from Java 8 on. */
  private static final class NaturalOrder implements Comparator<Object>, Serializable
  {
    @Override
    @SuppressWarnings("unchecked")
    public int compare(Object a, Object b)
    {
      return ((Comparable<Object>) a).compareTo(b);
    }
  }
}
