// Gather the Spark operators which return intermediates to local (actions/single_block)
// In addition count the number of Spark OPs underneath every Operator
public static int collectSparkRoots(Lop root, Map<Long, Integer> sparkOpCount, HashSet<Lop> sparkRoots) {
    return collectRoots(root, sparkOpCount, sparkRoots, Lop::isExecSpark, OperatorOrderingUtils::isSparkTriggeringOp);
}
// ---- helper method(s) introduced by the refactoring ----
private static int collectRoots(Lop root, Map<Long, Integer> opCount, HashSet<Lop> roots, LopPredicate isExecPredicate, LopPredicate isTriggeringPredicate) {
    if (//visited before
    opCount.containsKey(root.getID()))
        return opCount.get(root.getID());
    // Aggregate operator count in the child DAGs
    int total = 0;
    for (Lop input : root.getInputs()) total += collectRoots(input, opCount, roots, isExecPredicate, isTriggeringPredicate);
    // Check if this node is the desired execution type
    total = isExecPredicate.test(root) ? total + 1 : total;
    opCount.put(root.getID(), total);
    // Triggering point: Operator with all CP consumers
    if (isTriggeringPredicate.test(root)) {
        roots.add(root);
    }
    return total;
}

