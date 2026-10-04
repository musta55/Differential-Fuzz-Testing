protected void rRenameDataNodes(CNode node, HashMap<Long, String> newNames) {
    if (node.isVisited())
        return;
    //recursively process children
    for (CNode c : node.getInput()) rRenameDataNodes(c, newNames);
    //rename data node
    if (node instanceof CNodeData) {
        CNodeData dnode = (CNodeData) node;
        if (newNames.containsKey(dnode.getHopID()))
            dnode.setName(newNames.get(dnode.getHopID()));
    }
    node.resetHash();
    node.setVisited();
}