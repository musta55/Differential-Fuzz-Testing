protected final Visualizer getVisualizer() {
    if (listener == null) {
        // e.g. in non-GUI mode
        return null;
    }
    return listener.get();
}