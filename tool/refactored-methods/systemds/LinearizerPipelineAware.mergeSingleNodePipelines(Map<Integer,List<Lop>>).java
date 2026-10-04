private static void mergeSingleNodePipelines(Map<Integer, List<Lop>> map) {
    Map<Integer, List<Lop>> pipelinesWithOneNode = map.entrySet().stream().filter(e -> e.getValue().size() == 1).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    if (pipelinesWithOneNode.isEmpty())
        return;
    pipelinesWithOneNode.forEach((key, value) -> {
        Lop lop = value.get(0);
        if (!lop.getOutputs().isEmpty()) {
            lop.setPipelineID(lop.getOutputs().get(0).getPipelineID());
        } else if (!lop.getInputs().isEmpty()) {
            lop.setPipelineID(lop.getInputs().get(0).getPipelineID());
        }
        if (!lop.getOutputs().isEmpty() || !lop.getInputs().isEmpty()) {
            map.get(lop.getPipelineID()).add(lop);
            map.remove(key);
        }
    });
}