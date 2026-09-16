@PreDestroy
public void cleanup() {
    if (windowIdToRemove != null) {
        windowContext.closeWindow(windowIdToRemove);
    }
}