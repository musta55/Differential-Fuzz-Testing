protected static boolean equalInputReferences(ArrayList<CNode> current1, ArrayList<CNode> current2, ArrayList<CNode> input1, ArrayList<CNode> input2) {
    boolean ret = (current1.size() == current2.size());
    for (int i = 0; ret && i < current1.size(); i++) {
        ret &= equalInputReferences(current1.get(i), current2.get(i), input1, input2);
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

