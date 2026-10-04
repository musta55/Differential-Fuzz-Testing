// Step 3: Merge small pipelines into bigger ones
// Heuristic: Merge the smallest pipeline with the second smallest pipeline
// We don't care about whether the pipelines are connected or not
// This reduces the overhead as we avoid calculating the entire combinatorial problem space
// for finding an optimal solution.
// An optimal solution could be defined as a solution that reduces unnecessary overhead from
// too small pipelines (if executed in parallel, e.g., in a separate thread)
// and still find a maximum number of pipelines (for maximal parallelization)
// A proposed way to achieve a balance between avoiding too small pipelines and maximizing the number of pipelines:
// HARD_LIMIT: If the size of a pipeline is smaller than HARD_LIMIT, it will be merged with the next smallest pipeline.
// UPPER_BOUND: If the combined size of the two smallest pipelines is less than UPPER_BOUND, merge them.
// Return map by reference
private static void mergeSmallPipelines(Map<Integer, List<Lop>> map) {
    // Needs to have atleast two pipelines
    if (map.size() < 2)
        return;
    // Sort the pipelines by size
    List<Map.Entry<Integer, Integer>> sortedPipelineSizes = getPipelinesSortedBySize(map);
    Map.Entry<Integer, Integer> sm0 = sortedPipelineSizes.get(0);
    Map.Entry<Integer, Integer> sm1 = sortedPipelineSizes.get(1);
    while ((sm0 != null && sm1 != null) && (sm0.getValue() < HARD_LIMIT || sm0.getValue() + sm1.getValue() < UPPER_BOUND)) {
        // Merge pipelines as they satifiy the conditions
        int mergeIntoId = sm1.getKey();
        map.get(sm0.getKey()).forEach(l -> l.setPipelineID(mergeIntoId));
        map.get(mergeIntoId).addAll(map.get(sm0.getKey()));
        map.remove(sm0.getKey());
        //Get new list of sizes from updated map!
        sortedPipelineSizes = getPipelinesSortedBySize(map);
        //Update sm0 and sm1, if possible
        if (sortedPipelineSizes.size() < 2) {
            sm0 = null;
            sm1 = null;
        } else {
            sm0 = sortedPipelineSizes.get(0);
            sm1 = sortedPipelineSizes.get(1);
        }
    }
}