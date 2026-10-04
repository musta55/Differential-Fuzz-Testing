protected void rRenameDataNode(List<CNode> roots, CNode input, String newName) {
    if (!(input instanceof CNodeData))
        return;
    //create temporary name mapping
    HashMap<Long, String> newNames = new HashMap<>();
    newNames.put(((CNodeData) input).getHopID(), newName);
    //single pass to replace all names
    resetVisitStatus(roots);
    for (CNode root : roots) rRenameDataNodes(root, newNames);
}