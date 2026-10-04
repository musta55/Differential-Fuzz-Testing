private void drawSample(long x, Sample oneSample, Graphics g) {
    int height = getHeight();
    log.debug("Drawing a sample at {}", x);
    // will always be within range of an int: as must be < width
    int adjustedWidth = (int) (x % WIDTH);
    if (wantData) {
        int data = (int) (oneSample.getData() * height / graphMax);
        if (oneSample.isSuccess()) {
            g.setColor(Color.black);
        } else {
            g.setColor(Color.YELLOW);
        }
        g.drawLine(adjustedWidth, height - data, adjustedWidth, height - data - 1);
        if (log.isDebugEnabled()) {
            log.debug("Drawing coords = {}, {}", adjustedWidth, height - data);
        }
    }
    if (wantAverage) {
        int average = (int) (oneSample.getAverage() * height / graphMax);
        g.setColor(Color.blue);
        g.drawLine(adjustedWidth, height - average, adjustedWidth, height - average - 1);
    }
    if (wantMedian) {
        int median = (int) (oneSample.getMedian() * height / graphMax);
        g.setColor(JMeterColor.PURPLE);
        g.drawLine(adjustedWidth, height - median, adjustedWidth, height - median - 1);
    }
    if (wantDeviation) {
        int deviation = (int) (oneSample.getDeviation() * height / graphMax);
        g.setColor(Color.red);
        g.drawLine(adjustedWidth, height - deviation, adjustedWidth, height - deviation - 1);
    }
    if (wantThroughput) {
        int throughput = (int) (oneSample.getThroughput() * height / throughputMax);
        g.setColor(JMeterColor.DARK_GREEN);
        g.drawLine(adjustedWidth, height - throughput, adjustedWidth, height - throughput - 1);
    }
}