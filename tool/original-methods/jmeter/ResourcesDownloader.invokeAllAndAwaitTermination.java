/**
 * This method will block until the downloads complete or it get interrupted
 * the Future list returned by this method only contains tasks that have been scheduled in the threadpool.<br>
 * The status of those futures are either done or cancelled
 *
 * @param maxConcurrentDownloads max concurrent downloads
 * @param list                   list of resources to download
 * @return list tasks that have been scheduled
 * @throws InterruptedException when interrupted while waiting
 */
public List<Future<AsynSamplerResultHolder>> invokeAllAndAwaitTermination(int maxConcurrentDownloads, List<Callable<AsynSamplerResultHolder>> list) throws InterruptedException {
    List<Future<AsynSamplerResultHolder>> submittedTasks = new ArrayList<>();
    // paranoid fast path
    if (list.isEmpty()) {
        return submittedTasks;
    }
    // restore MaximumPoolSize original value
    concurrentExecutor.setMaximumPoolSize(MAX_POOL_SIZE);
    if (LOG.isDebugEnabled()) {
        LOG.debug("PoolSize={} LargestPoolSize={}", concurrentExecutor.getPoolSize(), concurrentExecutor.getLargestPoolSize());
    }
    CompletionService<AsynSamplerResultHolder> completionService = new ExecutorCompletionService<>(concurrentExecutor);
    int remainingTasksToTake = list.size();
    try {
        // push the task in the threadpool until <maxConcurrentDownloads> is reached
        int i = 0;
        for (i = 0; i < Math.min(maxConcurrentDownloads, list.size()); i++) {
            Callable<AsynSamplerResultHolder> task = list.get(i);
            submittedTasks.add(completionService.submit(task));
        }
        // push the remaining tasks but ensure we use at most <maxConcurrentDownloads> threads
        // wait for a previous download to finish before submitting a new one
        for (; i < list.size(); i++) {
            Callable<AsynSamplerResultHolder> task = list.get(i);
            try {
                completionService.take().get();
            } catch (ExecutionException e) {
                throw new RuntimeException("Task execution failed", e.getCause());
            }
            remainingTasksToTake--;
            submittedTasks.add(completionService.submit(task));
        }
        // all the resources downloads are in the thread pool queue
        // wait for the completion of all downloads
        while (remainingTasksToTake > 0) {
            try {
                completionService.take().get();
            } catch (ExecutionException e) {
                throw new RuntimeException("Task execution failed", e.getCause());
            }
            remainingTasksToTake--;
        }
    } finally {
        //bug 51925 : Calling Stop on Test leaks executor threads when concurrent download of resources is on
        if (remainingTasksToTake > 0) {
            LOG.debug("Interrupted while waiting for resource downloads : cancelling remaining tasks");
            for (Future<AsynSamplerResultHolder> future : submittedTasks) {
                if (!future.isDone()) {
                    future.cancel(true);
                }
            }
        }
    }
    return submittedTasks;
}