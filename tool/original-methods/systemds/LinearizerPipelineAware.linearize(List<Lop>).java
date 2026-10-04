/**
 * Sort lops depth-first while assigning the nodes to pipelines
 *
 * @param v List of lops to sort
 * @return Sorted list of lops with set _pipelineID on the Lop Object
 */
@Override
public List<Lop> linearize(List<Lop> v) {
    // If size of DAG is smaller than IGNORE_LIMIT, give all nodes the same pipeline id
    if (v.size() <= IGNORE_LIMIT) {
        v.forEach(l -> l.setPipelineID(1));
        return new LinearizerDepthFirst().linearize(v);
    }
    // Find all root nodes (starting points for the depth-first traversal)
    List<Lop> roots = v.stream().filter(OperatorOrderingUtils::isLopRoot).collect(Collectors.toList());
    // Initialize necessary data objects
    Integer pipelineId = 0;
    // Stores a resulting depth first sorted list of lops (same as in depthFirst())
    // Returned by this function
    ArrayList<Lop> opList = new ArrayList<>();
    // Stores the pipeline ids and the corresponding lops
    // for further refinement of pipeline assignements
    Map<Integer, List<Lop>> pipelineMap = new HashMap<>();
    // Step 1: Depth-first assignment of pipeline ids to the roots
    for (Lop r : roots) {
        pipelineId = depthFirst(r, pipelineId, opList, pipelineMap) + 1;
    }
    //DEVPrintDAG.asGraphviz("Step1", v);
    // Step 2: Merge pipelines with only one node to another (connected) pipeline
    LinearizerPipelineAware.mergeSingleNodePipelines(pipelineMap);
    //DEVPrintDAG.asGraphviz("Step2", v);
    // Step 3: Merge small pipelines into bigger ones
    LinearizerPipelineAware.mergeSmallPipelines(pipelineMap);
    //DEVPrintDAG.asGraphviz("Step3", v);
    // Reset the visited status of all nodes
    roots.forEach(Lop::resetVisitStatus);
    return opList;
}