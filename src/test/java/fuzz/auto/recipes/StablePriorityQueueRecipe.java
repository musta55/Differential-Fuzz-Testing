package fuzz.auto.recipes;

import com.code_intelligence.jazzer.api.FuzzedDataProvider;

/**
 * StablePriorityQueue: every constructor ends in new PriorityQueue(capacity, ...), which rejects a
 * capacity below 1, and a short input gives 0 (or an empty collection), so the queue was rarely
 * built (toArray: 0 of 233). This makes one with capacity 16 and adds 0-5 Integers, which sort
 * naturally. A used-up input reads as 0 everywhere: five equal elements, the case a stable queue
 * exists for.
 */
final class StablePriorityQueueRecipe implements Recipe
{
  @Override
  public Object build(FuzzedDataProvider data, Class<?> type) throws Exception
  {
    Object queue = type.getConstructor(int.class).newInstance(16);
    int n = 5 - data.consumeInt(0, 5);
    for (int i = 0; i < n; i++) {
      type.getMethod("add", Object.class).invoke(queue, data.consumeInt(0, 9));
    }
    return queue;
  }
}
