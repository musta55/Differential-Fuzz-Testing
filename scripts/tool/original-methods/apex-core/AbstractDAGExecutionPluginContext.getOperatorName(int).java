@Override
public String getOperatorName(int id) {
    PTOperator ptOperator = dnmgr.getPhysicalPlan().getAllOperators().get(id);
    if (ptOperator != null) {
        return ptOperator.getName();
    }
    return null;
}