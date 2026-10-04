public void rReorderCommutativeBinaryOps(CNode node, long mainHopID) {
    if (isVisited())
        return;
    for (CNode c : node.getInput()) rReorderCommutativeBinaryOps(c, mainHopID);
    if (node instanceof CNodeBinary && node.getInput().get(1) instanceof CNodeData && ((CNodeData) node.getInput().get(1)).getHopID() == mainHopID && ((CNodeBinary) node).getType().isCommutative()) {
        CNode tmp = node.getInput().get(0);
        node.getInput().set(0, node.getInput().get(1));
        node.getInput().set(1, tmp);
    }
    setVisited();
}