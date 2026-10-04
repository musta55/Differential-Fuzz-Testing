private static void mergeSmallPipelines(Map<Integer, List<Lop>> map) {
    if (map.size() < 2)
        return;
    List<Map.Entry<Integer, Integer>> sortedPipelineSizes = getPipelinesSortedBySize(map);
    Map.Entry<Integer, Integer> sm0 = sortedPipelineSizes.get(0);
    Map.Entry<Integer, Integer> sm1 = sortedPipelineSizes.get(1);
    while (sm0 != null && sm1 != null && (sm0.getValue() < HARD_LIMIT || sm0.getValue() + sm1.getValue() < UPPER_BOUND)) {
        int mergeIntoId = sm1.getKey();
        map.get(sm0.getKey()).forEach(l -> l.setPipelineID(mergeIntoId));
        map.get(mergeIntoId).addAll(map.get(sm0.getKey()));
        map.remove(sm0.getKey());
        sortedPipelineSizes = getPipelinesSortedBySize(map);
        if (sortedPipelineSizes.size() < 2) {
            sm0 = null;
            sm1 = null;
        } else {
            sm0 = sortedPipelineSizes.get(0);
            sm1 = sortedPipelineSizes.get(1);
        }
    }
}