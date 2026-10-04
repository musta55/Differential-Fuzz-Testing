public HashSet<Long> getInputHopIDs(boolean inclLiterals) {
    HashSet<Long> ret = new HashSet<>();
    for (CNode input : _inputs) if (!input.isLiteral() || inclLiterals)
        ret.add(((CNodeData) input).getHopID());
    return ret;
}