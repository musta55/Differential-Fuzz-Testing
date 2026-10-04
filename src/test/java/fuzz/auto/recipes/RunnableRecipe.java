package fuzz.auto.recipes;

import com.code_intelligence.jazzer.api.FuzzedDataProvider;

/**
 * Builds a harmless Runnable. Left to autofuzz, a Runnable could be any implementation on the
 * class path; for NameableThreadFactory.newThread it picked StramLocalCluster, which started a
 * Jetty server that waited for a password on the console until the run was killed.
 */
final class RunnableRecipe implements Recipe
{
  @Override
  public Object build(FuzzedDataProvider data, Class<?> type)
  {
    return new Task(data.consumeInt());
  }

  /** Does nothing when run; the id gives two built tasks comparable content. */
  static final class Task implements Runnable
  {
    final int id;

    Task(int id)
    {
      this.id = id;
    }

    @Override
    public void run()
    {
    }
  }
}
