protected void renameInputs(List<CNode> outputs, ArrayList<CNode> inputs, int startIndex) {
    //create map of hopID to new names used for code generation
    HashMap<Long, String> newNames = new HashMap<>();
    for (int i = startIndex, sPos = 0, mPos = 0; i < inputs.size(); i++) {
        CNode cnode = inputs.get(i);
        if (cnode instanceof CNodeData && ((CNodeData) cnode).isLiteral())
            continue;
        newNames.put(((CNodeData) cnode).getHopID(), cnode.getDataType().isScalar() ? "scalars[" + mPos++ + "]" : "b[" + sPos++ + "]");
    }
    //single pass to replace all names
    resetVisitStatus(outputs);
    for (CNode output : outputs) rRenameDataNodes(output, newNames);
}