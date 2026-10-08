package fuzz.auto.recipes;

import java.lang.reflect.Constructor;
import java.lang.reflect.Proxy;

import com.code_intelligence.jazzer.api.FuzzedDataProvider;

/**
 * Quartz objects for job factories (deltaspike's CdiAwareJobFactory.newJob). Reflection only:
 * this folder is compiled for every project.
 */
final class QuartzRecipes
{
  private QuartzRecipes() {}

  /**
   * TriggerFiredBundle: a fired trigger for a job of class SimpleSchedulerJob1 (a deltaspike
   * example job with a public no-argument constructor). Quartz's default job factory creates the
   * job from that class and reads the trigger's job data, so the trigger is a real SimpleTriggerImpl.
   */
  static final class Bundle implements Recipe
  {
    @Override
    public Object build(FuzzedDataProvider data, Class<?> type) throws Exception
    {
      ClassLoader loader = type.getClassLoader();
      Class<?> jobClass = Class.forName("org.apache.deltaspike.example.scheduler.SimpleSchedulerJob1",
          true, loader);
      Class<?> builderType = Class.forName("org.quartz.JobBuilder", true, loader);
      Object builder = builderType.getMethod("newJob", Class.class).invoke(null, jobClass);
      Object jobDetail = builderType.getMethod("build").invoke(builder);
      Object trigger = Class.forName("org.quartz.impl.triggers.SimpleTriggerImpl", true, loader)
          .getConstructor().newInstance();

      // (JobDetail, OperableTrigger, Calendar, boolean recovering, Date x4)
      Constructor<?> ctor = type.getConstructors()[0];
      Object[] args = new Object[ctor.getParameterCount()];
      args[0] = jobDetail;
      args[1] = trigger;
      args[3] = false;
      return ctor.newInstance(args);
    }
  }

  /** Scheduler: an interface; the stand-in only answers getContext() with a real SchedulerContext. */
  static final class Scheduler implements Recipe
  {
    @Override
    public Object build(FuzzedDataProvider data, Class<?> type) throws Exception
    {
      final Object context = Class.forName("org.quartz.SchedulerContext", true, type.getClassLoader())
          .getConstructor().newInstance();
      return Proxy.newProxyInstance(type.getClassLoader(), new Class<?>[] {type},
          new ServletRecipes.Handler()
          {
            @Override
            Object call(String name, Object[] args)
            {
              return name.equals("getContext") ? context : null;
            }

            @Override
            public String toString()
            {
              return "Scheduler";
            }
          });
    }
  }
}
