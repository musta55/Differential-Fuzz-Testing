package fuzz.auto.recipes;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import com.code_intelligence.jazzer.api.FuzzedDataProvider;

/**
 * MetricAggregatorMeta(Aggregator, DimensionsScheme) takes two Apex API interfaces the engine has
 * no implementation for, so it was never built. Its getters only use the DimensionsScheme, and
 * behave differently when it is null, so the scheme is sometimes null and otherwise a stand-in
 * returning fuzzed strings. The aggregator is never used by them and stays null.
 */
final class MetricAggregatorMetaRecipe implements Recipe
{
  @Override
  public Object build(FuzzedDataProvider data, Class<?> type) throws Exception
  {
    ClassLoader loader = type.getClassLoader();
    Class<?> aggregatorType = Class.forName("com.datatorrent.api.AutoMetric$Aggregator", true, loader);
    Class<?> schemeType = Class.forName("com.datatorrent.api.AutoMetric$DimensionsScheme", true, loader);

    Object scheme = null;
    if (data.consumeBoolean()) {
      final String[] values = new String[data.consumeInt(0, 3)];
      for (int i = 0; i < values.length; i++) {
        values[i] = data.consumeAsciiString(8);
      }
      scheme = Proxy.newProxyInstance(loader, new Class<?>[] {schemeType}, new InvocationHandler()
      {
        @Override
        public Object invoke(Object proxy, Method m, Object[] args)
        {
          // getTimeBuckets() and getDimensionAggregationsFor(name) both return a String[].
          return m.getReturnType() == String[].class ? values.clone() : null;
        }
      });
    }

    Constructor<?> c = type.getDeclaredConstructor(aggregatorType, schemeType);
    c.setAccessible(true); // the constructor is protected
    return c.newInstance(null, scheme);
  }
}
