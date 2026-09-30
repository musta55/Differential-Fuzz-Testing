@Override
public String getOperatorName(int id) {
    PTOperator ptOperator = dnmgr.getPhysicalPlan().getAllOperators().get(id);
    return ptOperator != null ? ptOperator.getName() : null;
}