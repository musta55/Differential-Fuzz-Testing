/**
 * Sort lops depth-first while assigning the nodes to pipelines
 *
 * @param v List of lops to sort
 * @return Sorted list of lops with set _pipelineID on the Lop Object
 */
@Override
public List<Lop> linearize(List<Lop> v) {
    if (v.size() <= IGNORE_LIMIT) {
        v.forEach(l -> l.setPipelineID(1));
        return new LinearizerDepthFirst().linearize(v);
    }
    List<Lop> roots = v.stream().filter(OperatorOrderingUtils::isLopRoot).collect(Collectors.toList());
    int pipelineId = 0;
    List<Lop> opList = new ArrayList<>();
    Map<Integer, List<Lop>> pipelineMap = new HashMap<>();
    for (Lop r : roots) {
        pipelineId = depthFirst(r, pipelineId, opList, pipelineMap) + 1;
    }
    mergeSingleNodePipelines(pipelineMap);
    mergeSmallPipelines(pipelineMap);
    roots.forEach(Lop::resetVisitStatus);
    return opList;
}