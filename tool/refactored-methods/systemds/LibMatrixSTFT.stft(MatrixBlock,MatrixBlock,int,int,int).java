/**
 * Function to perform STFT on two given matrices with windowSize and overlap. The first one represents the real
 * values and the second one the imaginary values. The output also contains one matrix for the real and one for the
 * imaginary values. The results of the fourier transformations are appended to each other in the output.
 *
 * @param re         Matrix object representing the real values
 * @param im         Matrix object representing the imaginary values
 * @param windowSize Size of window
 * @param overlap    Size of overlap
 * @param threads    The number of threads to use
 * @return array of two matrix blocks
 */
public static MatrixBlock[] stft(MatrixBlock re, MatrixBlock im, int windowSize, int overlap, int threads) {
    int rows = re.getNumRows();
    int cols = re.getNumColumns();
    int stepSize = calculateStepSize(windowSize, overlap);
    int numberOfFramesPerRow = calculateNumberOfFrames(cols, windowSize, overlap, stepSize);
    int rowLength = numberOfFramesPerRow * windowSize;
    int out_len = rowLength * rows;
    double[] stftOutput_re = new double[out_len];
    double[] stftOutput_im = new double[out_len];
    double[] re_inter = new double[out_len];
    double[] im_inter = new double[out_len];
    ExecutorService pool = CommonThreadPool.get(threads);
    List<Future<?>> tasks = new ArrayList<>();
    try {
        submitTasks(pool, tasks, re, im, stftOutput_re, stftOutput_im, re_inter, im_inter, rows, cols, numberOfFramesPerRow, rowLength, windowSize);
        waitForTasksCompletion(tasks);
    } catch (InterruptedException | ExecutionException e) {
        throw new RuntimeException(e);
    } finally {
        pool.shutdown();
    }
    return new MatrixBlock[] { new MatrixBlock(rows, rowLength, stftOutput_re), new MatrixBlock(rows, rowLength, stftOutput_im) };
}
// ---- helper method(s) introduced by the refactoring ----
private static int calculateStepSize(int windowSize, int overlap) {
    int stepSize = windowSize - overlap;
    if (stepSize == 0) {
        throw new IllegalArgumentException("windowSize - overlap is zero");
    }
    return stepSize;
}

private static int calculateNumberOfFrames(int cols, int windowSize, int overlap, int stepSize) {
    return (cols - overlap + stepSize - 1) / stepSize;
}

private static void submitTasks(ExecutorService pool, List<Future<?>> tasks, MatrixBlock re, MatrixBlock im, double[] stftOutput_re, double[] stftOutput_im, double[] re_inter, double[] im_inter, int rows, int cols, int numberOfFramesPerRow, int rowLength, int windowSize) {
    for (int h = 0; h < rows; h++) {
        final int finalH = h;
        tasks.add(pool.submit(() -> processRow(finalH, re, im, stftOutput_re, stftOutput_im, re_inter, im_inter, cols, numberOfFramesPerRow, rowLength, windowSize)));
    }
}

private static void processRow(int h, MatrixBlock re, MatrixBlock im, double[] stftOutput_re, double[] stftOutput_im, double[] re_inter, double[] im_inter, int cols, int numberOfFramesPerRow, int rowLength, int windowSize) {
    for (int i = 0; i < numberOfFramesPerRow; i++) {
        copyDataToOutput(re, im, stftOutput_re, stftOutput_im, h, i, cols, rowLength, windowSize);
        fft_one_dim(stftOutput_re, stftOutput_im, re_inter, im_inter, h * rowLength + i * windowSize, h * rowLength + (i + 1) * windowSize, windowSize, 1);
    }
}

private static void copyDataToOutput(MatrixBlock re, MatrixBlock im, double[] stftOutput_re, double[] stftOutput_im, int h, int i, int cols, int rowLength, int windowSize) {
    for (int j = 0; j < windowSize; j++) {
        int index = h * cols + i * windowSize + j;
        int outputIndex = h * rowLength + i * windowSize + j;
        if (index < cols) {
            stftOutput_re[outputIndex] = re.getDenseBlockValues()[index];
            stftOutput_im[outputIndex] = im.getDenseBlockValues()[index];
        }
    }
}

private static void waitForTasksCompletion(List<Future<?>> tasks) throws InterruptedException, ExecutionException {
    for (Future<?> f : tasks) {
        f.get();
    }
}

