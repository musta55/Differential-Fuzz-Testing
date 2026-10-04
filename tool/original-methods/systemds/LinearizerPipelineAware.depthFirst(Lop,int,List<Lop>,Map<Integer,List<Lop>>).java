// Step 1: Depth-first assignment of pipeline ids to the roots
// Finds the branching out/in of Lops, that could be parallized
// (and with it assiging of different pipeline ids)
private static int depthFirst(Lop root, int pipelineId, List<Lop> opList, Map<Integer, List<Lop>> pipelineMap) {
    // Abort if the node was already visited
    if (root.isVisited()) {
        return root.getPipelineID();
    }
    // Assign pipeline id to the node, given by the parent
    // Set the root node as visited
    root.setPipelineID(pipelineId);
    root.setVisited();
    // Add the root node to the pipeline list
    if (pipelineMap.containsKey(pipelineId)) {
        pipelineMap.get(pipelineId).add(root);
    } else {
        ArrayList<Lop> lopList = new ArrayList<>();
        lopList.add(root);
        pipelineMap.put(pipelineId, lopList);
    }
    // Children as inputs, as we are traversing the lops bottom up
    List<Lop> children = root.getInputs();
    // If root node has only one child, use the same pipeline id as root node
    if (children.size() == 1) {
        Lop child = children.get(0);
        // We need to find the max pipeline id of the child, because the child could branch out
        pipelineId = Math.max(pipelineId, depthFirst(child, pipelineId, opList, pipelineMap));
    } else {
        // Iteration over all children
        for (int i = 0; i < children.size(); i++) {
            Lop child = children.get(i);
            // If the child has only one output, or all outputs are the root node, use the same pipeline id as parent
            if (child.getOutputs().size() == 1 || (child.getOutputs().size() > 1 && child.getOutputs().stream().allMatch(o -> o == root))) {
                // No need for max, because the child can only have one output
                depthFirst(child, root.getPipelineID(), opList, pipelineMap);
            } else {
                // We need to find the max pipeline id of the child, because the child could branch out
                pipelineId = Math.max(pipelineId, depthFirst(child, pipelineId + 1, opList, pipelineMap));
            }
        }
    }
    opList.add(root);
    return pipelineId;
}