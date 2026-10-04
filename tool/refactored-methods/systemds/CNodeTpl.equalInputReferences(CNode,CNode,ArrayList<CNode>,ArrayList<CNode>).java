protected static boolean equalInputReferences(CNode current1, CNode current2, ArrayList<CNode> input1, ArrayList<CNode> input2) {
    boolean ret = (current1.getInput().size() == current2.getInput().size());
    for (int i = 0; ret && i < current1.getInput().size(); i++) {
        ret &= equalInputReferences(current1.getInput().get(i), current2.getInput().get(i), input1, input2);
    }
    if (ret && current1 instanceof CNodeData) {
        ret &= current2 instanceof CNodeData && indexOf(input1, (CNodeData) current1) == indexOf(input2, (CNodeData) current2);
    }
    return ret;
}
// ---- helper method(s) introduced by the refactoring ----
private void validateInputs(ArrayList<CNode> inputs) {
    if (inputs.size() < 1) {
        throw new RuntimeException("Cannot pass empty inputs to the CNodeTpl");
    }
}

private HashMap<Long, String> createNewNamesMap(ArrayList<CNode> inputs, int startIndex) {
    HashMap<Long, String> newNames = new HashMap<>();
    for (int i = startIndex, sPos = 0, mPos = 0; i < inputs.size(); i++) {
        CNode cnode = inputs.get(i);
        if (cnode instanceof CNodeData && ((CNodeData) cnode).isLiteral()) {
            continue;
        }
        newNames.put(((CNodeData) cnode).getHopID(), cnode.getDataType().isScalar() ? "scalars[" + mPos++ + "]" : "b[" + sPos++ + "]");
    }
    return newNames;
}

private void reorderIfCommutative(CNode node, long mainHopID) {
    if (node instanceof CNodeBinary && node.getInput().get(1) instanceof CNodeData && ((CNodeData) node.getInput().get(1)).getHopID() == mainHopID && ((CNodeBinary) node).getType().isCommutative()) {
        CNode tmp = node.getInput().get(0);
        node.getInput().set(0, node.getInput().get(1));
        node.getInput().set(1, tmp);
    }
}

