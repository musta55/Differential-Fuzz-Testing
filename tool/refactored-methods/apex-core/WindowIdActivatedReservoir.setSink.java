@Override
public Sink<Object> setSink(Sink<Object> sink) {
    this.sink = sink;
    return this.sink;
}
// ---- helper method(s) introduced by the refactoring ----
private Object handleEndStreamTuple() {
    try {
        return est;
    } finally {
        est = null;
    }
}

private boolean shouldActivateWindow(Tuple t) {
    return t.getType() == MessageType.BEGIN_WINDOW && t.getWindowId() > windowId;
}

private void activateWindow() {
    reservoir.setSink(sink);
}

private EndStreamTuple createEndStreamTuple() {
    return est = new EndStreamTuple(windowId);
}

