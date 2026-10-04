public CNodeTpl eliminateCommonSubexpressions(CNodeTpl tpl) {
    //Note: Compared to our traditional common subexpression elimination, on cplans,
    //we don't have any parent references, and hence cannot use a collect-merge approach.
    //In contrast, we exploit the hash signatures of cnodes as used in the plan cache.
    //However, note that these signatures ignore input hops by default (for better plan
    //cache hit rates), but are temporarily set to strict evaluation for this rewrite.
    List<CNode> outputs = (tpl instanceof CNodeMultiAgg) ? ((CNodeMultiAgg) tpl).getOutputs() : Collections.singletonList(tpl.getOutput());
    //step 1: set data nodes to strict comparison
    tpl.resetVisitStatusOutputs();
    for (CNode out : outputs) rSetStrictDataNodeComparision(out, true);
    //step 2: perform common subexpression elimination
    HashMap<CNode, CNode> cseSet = new HashMap<>();
    tpl.resetVisitStatusOutputs();
    for (CNode out : outputs) rEliminateCommonSubexpression(out, cseSet);
    //step 3: reset data nodes to imprecise comparison
    tpl.resetVisitStatusOutputs();
    for (CNode out : outputs) rSetStrictDataNodeComparision(out, false);
    tpl.resetVisitStatusOutputs();
    return tpl;
}