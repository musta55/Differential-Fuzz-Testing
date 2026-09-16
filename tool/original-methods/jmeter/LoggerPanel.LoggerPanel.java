/**
 * Pane for display JMeter log file
 */
public LoggerPanel() {
    if (LOGGER_PANEL_MAX_LINES > 0) {
        events = new CircularFifoQueue<>(LOGGER_PANEL_MAX_LINES);
    } else {
        events = new ArrayDeque<>();
    }
    textArea = init();
}