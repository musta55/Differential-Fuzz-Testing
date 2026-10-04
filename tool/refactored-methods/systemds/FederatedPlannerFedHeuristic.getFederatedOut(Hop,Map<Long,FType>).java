@Override
protected FType getFederatedOut(Hop hop, Map<Long, FType> fedHops) {
    // FedAll
    FType ret = super.getFederatedOut(hop, fedHops);
    //apply operator-specific heuristics
    if (hop instanceof AggBinaryOp && shouldOverrideFederatedOutput(ret, hop)) {
        //get local vectors
        ret = null;
    }
    return ret;
}
// ---- helper method(s) introduced by the refactoring ----
private boolean shouldOverrideFederatedOutput(FType ret, Hop hop) {
    return (ret == FType.ROW && hop.getDim2() == 1) || (ret == FType.COL && hop.getDim1() == 1);
}

