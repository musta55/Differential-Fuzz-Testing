package fuzz.auto.recipes;

import java.lang.reflect.Proxy;
import java.util.Collections;

import com.code_intelligence.jazzer.api.FuzzedDataProvider;

/**
 * CDI Bean: normally made by a CDI container, so the engine could not build one
 * (ImmutableBeanWrapper's constructor). That constructor only asks it for getBeanClass() and
 * getInjectionPoints(), so this stand-in answers those: Object.class and no injection points.
 * Written with reflection because this folder is also compiled for projects without CDI.
 */
final class BeanRecipe implements Recipe
{
  @Override
  public Object build(FuzzedDataProvider data, Class<?> type)
  {
    return Proxy.newProxyInstance(type.getClassLoader(), new Class<?>[] {type},
        new ServletRecipes.Handler()
        {
          @Override
          Object call(String name, Object[] args)
          {
            if (name.equals("getBeanClass")) {
              return Object.class;
            }
            if (name.equals("getInjectionPoints")) {
              return Collections.emptySet();
            }
            return null;
          }

          @Override
          public String toString()
          {
            return "Bean(Object)";
          }
        });
  }
}
