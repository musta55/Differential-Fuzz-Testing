@Override
public StatsListener.BatchedOperatorStats getPhysicalOperatorStats(int id) {
    PTOperator ptOperator = dnmgr.getPhysicalPlan().getAllOperators().get(id);
    if (ptOperator != null) {
        return ptOperator.stats;
    }
    return null;
}