@Override
public StatsListener.BatchedOperatorStats getPhysicalOperatorStats(int id) {
    PTOperator ptOperator = dnmgr.getPhysicalPlan().getAllOperators().get(id);
    return ptOperator != null ? ptOperator.stats : null;
}