protected final Visualizer getVisualizer() {
    WeakReference<Visualizer> currentListener = listener;
    return currentListener != null ? currentListener.get() : null;
}