public void updateGui(final Sample oneSample) {
    long h = model.getPercentPoint((float) 0.90).longValue();
    boolean repaint = false;
    if ((oneSample.getCount() % 20 == 0 || oneSample.getCount() < 20) && h > (graphMax * 1.2) || graphMax > (h * 1.2)) {
        if (h >= 1) {
            graphMax = h;
        } else {
            graphMax = 1;
        }
        repaint = true;
    }
    if (model.getMaxThroughput() > throughputMax) {
        throughputMax = model.getMaxThroughput() * 1.3;
        repaint = true;
    }
    if (repaint) {
        repaint();
        return;
    }
    final long xPos = model.getCount();
    SwingUtilities.invokeLater(() -> {
        Graphics g = getGraphics();
        if (g != null) {
            drawSample(xPos, oneSample, g);
        }
    });
}