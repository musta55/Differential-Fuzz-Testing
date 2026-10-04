// Gather the Spark operators which return intermediates to local (actions/single_block)
// In addition count the number of Spark OPs underneath every Operator
public static int collectSparkRoots(Lop root, Map<Long, Integer> sparkOpCount, HashSet<Lop> sparkRoots) {
    if (//visited before
    sparkOpCount.containsKey(root.getID()))
        return sparkOpCount.get(root.getID());
    // Aggregate #Spark operators in the child DAGs
    int total = 0;
    for (Lop input : root.getInputs()) total += collectSparkRoots(input, sparkOpCount, sparkRoots);
    // Check if this node is Spark
    total = root.isExecSpark() ? total + 1 : total;
    sparkOpCount.put(root.getID(), total);
    // Triggering point: Spark action/operator with all CP consumers
    if (isSparkTriggeringOp(root)) {
        sparkRoots.add(root);
    }
    return total;
}