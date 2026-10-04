// previously called doTopologicalSortTwoLevelOrder
@Override
public List<Lop> linearize(List<Lop> v) {
    // partition nodes into leaf/inner nodes and dag root nodes,
    // + sort leaf/inner nodes by ID to force depth-first scheduling
    // + append root nodes in order of their original definition
    // (which also preserves the original order of prints)
    List<Lop> nodes = Stream.concat(v.stream().filter(l -> !l.getOutputs().isEmpty()).sorted(Comparator.comparing(Lop::getID)), v.stream().filter(l -> l.getOutputs().isEmpty())).collect(Collectors.toList());
    // NOTE: in contrast to hadoop execution modes, we avoid computing the transitive
    // closure here to ensure linear time complexity because its unnecessary for CP and Spark
    return nodes;
}