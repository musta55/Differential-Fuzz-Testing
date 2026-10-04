private static int indexOf(ArrayList<CNode> inputs, CNodeData probe) {
    for (int i = 0; i < inputs.size(); i++) {
        CNodeData cd = ((CNodeData) inputs.get(i));
        if (cd.getHopID() == probe.getHopID())
            return i;
    }
    return -1;
}