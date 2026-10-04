private void drawSample(long x, Sample sample, Graphics g) {
    int height = getHeight();
    log.debug("Drawing a sample at {}", x);
    int adjustedWidth = (int) (x % WIDTH);
    drawData(sample, adjustedWidth, height, g);
    drawAverage(sample, adjustedWidth, height, g);
    drawMedian(sample, adjustedWidth, height, g);
    drawDeviation(sample, adjustedWidth, height, g);
    drawThroughput(sample, adjustedWidth, height, g);
}
// ---- helper method(s) introduced by the refactoring ----
private void drawData(Sample sample, int adjustedWidth, int height, Graphics g) {
    if (!wantData)
        return;
    int data = (int) (sample.getData() * height / graphMax);
    g.setColor(sample.isSuccess() ? Color.BLACK : Color.YELLOW);
    g.drawLine(adjustedWidth, height - data, adjustedWidth, height - data - 1);
    if (log.isDebugEnabled()) {
        log.debug("Drawing coords = {}, {}", adjustedWidth, height - data);
    }
}

private void drawAverage(Sample sample, int adjustedWidth, int height, Graphics g) {
    if (!wantAverage)
        return;
    int average = (int) (sample.getAverage() * height / graphMax);
    g.setColor(Color.BLUE);
    g.drawLine(adjustedWidth, height - average, adjustedWidth, height - average - 1);
}

private void drawMedian(Sample sample, int adjustedWidth, int height, Graphics g) {
    if (!wantMedian)
        return;
    int median = (int) (sample.getMedian() * height / graphMax);
    g.setColor(JMeterColor.PURPLE);
    g.drawLine(adjustedWidth, height - median, adjustedWidth, height - median - 1);
}

private void drawDeviation(Sample sample, int adjustedWidth, int height, Graphics g) {
    if (!wantDeviation)
        return;
    int deviation = (int) (sample.getDeviation() * height / graphMax);
    g.setColor(Color.RED);
    g.drawLine(adjustedWidth, height - deviation, adjustedWidth, height - deviation - 1);
}

private void drawThroughput(Sample sample, int adjustedWidth, int height, Graphics g) {
    if (!wantThroughput)
        return;
    int throughput = (int) (sample.getThroughput() * height / throughputMax);
    g.setColor(JMeterColor.DARK_GREEN);
    g.drawLine(adjustedWidth, height - throughput, adjustedWidth, height - throughput - 1);
}

