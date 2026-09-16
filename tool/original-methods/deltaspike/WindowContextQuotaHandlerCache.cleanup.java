@PreDestroy
public void cleanup() {
    if (this.windowIdToRemove != null) {
        this.windowContext.closeWindow(this.windowIdToRemove);
    }
}