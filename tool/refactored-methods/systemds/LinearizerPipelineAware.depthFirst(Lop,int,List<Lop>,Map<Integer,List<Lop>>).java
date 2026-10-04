private static int depthFirst(Lop root, int pipelineId, List<Lop> opList, Map<Integer, List<Lop>> pipelineMap) {
    if (root.isVisited()) {
        return root.getPipelineID();
    }
    root.setPipelineID(pipelineId);
    root.setVisited();
    pipelineMap.computeIfAbsent(pipelineId, k -> new ArrayList<>()).add(root);
    List<Lop> children = root.getInputs();
    if (children.size() == 1) {
        pipelineId = Math.max(pipelineId, depthFirst(children.get(0), pipelineId, opList, pipelineMap));
    } else {
        for (Lop child : children) {
            if (child.getOutputs().size() == 1 || child.getOutputs().stream().allMatch(o -> o == root)) {
                depthFirst(child, root.getPipelineID(), opList, pipelineMap);
            } else {
                pipelineId = Math.max(pipelineId, depthFirst(child, pipelineId + 1, opList, pipelineMap));
            }
        }
    }
    opList.add(root);
    return pipelineId;
}