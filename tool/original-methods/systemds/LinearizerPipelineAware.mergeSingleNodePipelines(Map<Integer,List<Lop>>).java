// Step 2: Merge pipelines with only one node to another (connected) pipeline
// Return map by reference
private static void mergeSingleNodePipelines(Map<Integer, List<Lop>> map) {
    Map<Integer, List<Lop>> pipelinesWithOneNode = map.entrySet().stream().filter(e -> e.getValue().size() == 1).collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue()));
    if (pipelinesWithOneNode.size() == 0)
        return;
    pipelinesWithOneNode.entrySet().stream().forEach(e -> {
        Lop lop = e.getValue().get(0);
        // Merge to an existing output node
        if (lop.getOutputs().size() > 0) {
            lop.setPipelineID(lop.getOutputs().get(0).getPipelineID());
            // If no outputs are present, merge to an existing input node
        } else if (lop.getInputs().size() > 0) {
            lop.setPipelineID(lop.getInputs().get(0).getPipelineID());
        }
        // else (no inputs and no outputs): do nothing (unreachable node?)
        // Remove the pipeline from the list of pipelines
        if (lop.getOutputs().size() > 0 || lop.getInputs().size() > 0) {
            map.get(lop.getPipelineID()).add(lop);
            map.remove(e.getKey());
        }
    });
}