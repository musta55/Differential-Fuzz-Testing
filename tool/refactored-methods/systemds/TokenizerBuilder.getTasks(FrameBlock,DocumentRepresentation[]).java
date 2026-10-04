public List<DependencyTask<?>> getTasks(FrameBlock in, DocumentRepresentation[] internalRepresentation) {
    int nRows = in.getNumRows();
    List<Callable<Object>> tasks = new ArrayList<>();
    int[] blockSizes = getBlockSizes(nRows, TOKENIZE_NUM_BLOCKS);
    if (blockSizes.length == 1) {
        tasks.add(createSingleTask(in, internalRepresentation, 0, -1));
    } else {
        tasks.addAll(createMultipleTasks(in, internalRepresentation, blockSizes));
    }
    return DependencyThreadPool.createDependencyTasks(tasks, null);
}
// ---- helper method(s) introduced by the refactoring ----
private Callable<Object> createSingleTask(FrameBlock in, DocumentRepresentation[] internalRepresentation, int rowStart, int blk) {
    return new TokenizerBuildTask<>(this, in, internalRepresentation, rowStart, blk);
}

private List<Callable<Object>> createMultipleTasks(FrameBlock in, DocumentRepresentation[] internalRepresentation, int[] blockSizes) {
    List<Callable<Object>> tasks = new ArrayList<>();
    for (int startRow = 0, i = 0; i < blockSizes.length; startRow += blockSizes[i], i++) {
        tasks.add(new TokenizerBuildTask<>(this, in, internalRepresentation, startRow, blockSizes[i]));
    }
    return tasks;
}

