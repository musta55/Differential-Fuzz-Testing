package fuzz.auto.recipes;

import java.lang.reflect.Constructor;

import com.code_intelligence.jazzer.api.FuzzedDataProvider;

/**
 * AbstractInvocationContext: its concrete subclass DeltaSpikeProxyInvocationContext takes a proxy
 * handler, a BeanManager, interceptors, a target, a method, parameters and a timer, which the
 * engine could not build. The constructor only stores them, and getContextData() uses none of
 * them, so all of them are null.
 */
final class InvocationContextRecipe implements Recipe
{
  @Override
  public Object build(FuzzedDataProvider data, Class<?> type) throws Exception
  {
    Class<?> contextType = Class.forName(
        "org.apache.deltaspike.proxy.spi.invocation.DeltaSpikeProxyInvocationContext", true,
        type.getClassLoader());
    Constructor<?> ctor = contextType.getConstructors()[0];
    return ctor.newInstance(new Object[ctor.getParameterCount()]);
  }
}
