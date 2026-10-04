public List<DependencyTask<?>> getTasks(FrameBlock in, DocumentRepresentation[] internalRepresentation) {
    int nRows = in.getNumRows();
    List<Callable<Object>> tasks = new ArrayList<>();
    int[] blockSizes = getBlockSizes(nRows, TOKENIZE_NUM_BLOCKS);
    if (blockSizes.length == 1) {
        tasks.add(new TokenizerBuildTask<>(this, in, internalRepresentation, 0, -1));
    } else {
        for (int startRow = 0, i = 0; i < blockSizes.length; startRow += blockSizes[i], i++) {
            tasks.add(new TokenizerBuildTask<>(this, in, internalRepresentation, startRow, blockSizes[i]));
        }
    }
    return DependencyThreadPool.createDependencyTasks(tasks, null);
}