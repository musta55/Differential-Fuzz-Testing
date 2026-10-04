// Gather the GPU operators which return intermediate to host
// In addition count the number of GPU OPs below every operator
public static int collectGPURoots(Lop root, Map<Long, Integer> gpuOpCount, HashSet<Lop> gpuRoots) {
    if (//visited before
    gpuOpCount.containsKey(root.getID()))
        return gpuOpCount.get(root.getID());
    // Aggregate GPU operator count in the child DAGs
    int total = 0;
    for (Lop input : root.getInputs()) total += collectSparkRoots(input, gpuOpCount, gpuRoots);
    // Check if this node is GPU
    total = root.isExecGPU() ? total + 1 : total;
    gpuOpCount.put(root.getID(), total);
    // Triggering point: Spark action/operator with all CP consumers
    if (isD2HCopyOp(root))
        gpuRoots.add(root);
    return total;
}