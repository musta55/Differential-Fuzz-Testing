package fuzz.auto.recipes;

import java.util.Map;

import com.code_intelligence.jazzer.api.FuzzedDataProvider;

/**
 * For GroupingManager.moveOperatorFromUndeployListToDeployList(PTOperator): a PTOperator only
 * exists inside a physical plan, and the method only does something when grouping requests list
 * that operator.
 */
final class GroupingRecipes
{
  private GroupingRecipes() {}

  /** The single operator (id 1) of the one-operator application from PluginContextRecipe. */
  static final class Operator implements Recipe
  {
    @Override
    public Object build(FuzzedDataProvider data, Class<?> type) throws Exception
    {
      Object manager = PluginContextRecipe.manager(type.getClassLoader());
      Object plan = manager.getClass().getMethod("getPhysicalPlan").invoke(manager);
      Map<?, ?> operators = (Map<?, ?>) plan.getClass().getMethod("getAllOperators").invoke(plan);
      return operators.values().iterator().next();
    }
  }

  /**
   * A GroupingManager with 0-3 grouping requests, each listing operator 1 or another operator for
   * undeployment. A used-up input reads as 0 everywhere, which gives three requests that all list
   * operator 1: the case where moving it in every request and moving it in the first one differ.
   */
  static final class Manager implements Recipe
  {
    @Override
    public Object build(FuzzedDataProvider data, Class<?> type) throws Exception
    {
      Object manager = type.getConstructor().newInstance();
      @SuppressWarnings("unchecked")
      Map<String, Object> requests =
          (Map<String, Object>) type.getMethod("getGroupingRequests").invoke(manager);
      Class<?> requestType = Class.forName("org.apache.apex.engine.events.grouping.GroupingRequest",
          true, type.getClassLoader());
      int n = 3 - data.consumeInt(0, 3);
      for (int i = 0; i < n; i++) {
        Object request = requestType.getConstructor().newInstance();
        int operatorId = data.consumeBoolean() ? 2 : 1;
        requestType.getMethod("addOperatorToUndeploy", int.class).invoke(request, operatorId);
        requests.put("container-" + i, request);
      }
      return manager;
    }
  }
}
