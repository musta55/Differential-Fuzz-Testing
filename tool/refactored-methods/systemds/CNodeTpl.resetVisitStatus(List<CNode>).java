public static void resetVisitStatus(List<CNode> outputs) {
    for (CNode output : outputs) {
        output.resetVisitStatus();
    }
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

