@Override
public Tuple sweep() {
    Tuple t;
    while ((t = reservoir.sweep()) != null) {
        if (t.getType() == MessageType.BEGIN_WINDOW && t.getWindowId() > windowId) {
            reservoir.setSink(sink);
            return (est = new EndStreamTuple(windowId));
        }
        reservoir.remove();
    }
    return null;
}