private void updateLogEntries() {
    if (!logChanged) {
        return;
    }
    logChanged = false;
    StringBuilder builder = new StringBuilder();
    synchronized (events) {
        for (String line : events) {
            builder.append(line);
        }
    }
    String logText = builder.toString();
    synchronized (textArea) {
        if (LOGGER_PANEL_MAX_LINES > 0) {
            textArea.setText(logText);
        } else {
            textArea.append(logText);
        }
        textArea.setCaretPosition(textArea.getText().length());
    }
}