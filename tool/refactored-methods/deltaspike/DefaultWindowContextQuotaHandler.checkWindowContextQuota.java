public synchronized /*no issue due to session-scoped instance*/
void checkWindowContextQuota(String windowId) {
    if (windowId == null) {
        return;
    }
    if (quotaHandlerCache.cacheWindowId(windowId)) {
        return;
    }
    updateWindowIdStack(windowId);
}
// ---- helper method(s) introduced by the refactoring ----
private void updateWindowIdStack(String windowId) {
    if (windowIdStack.contains(windowId)) {
        moveWindowIdToFront(windowId);
    } else {
        addNewWindowId(windowId);
    }
}

private void moveWindowIdToFront(String windowId) {
    if (//don't move it up if there is just one entry
    windowIdStack.size() > 1) {
        windowIdStack.remove(windowId);
        windowIdStack.push(windowId);
    }
}

private void addNewWindowId(String windowId) {
    windowIdStack.push(windowId);
    if (windowIdStack.size() > maxWindowContextCount) {
        String windowIdToRemove = windowIdStack.remove(0);
        //destroy it lazily at the end of the request to avoid an overhead during the request
        //which might be caused by pre-destroy logic of window-scoped beans
        quotaHandlerCache.setWindowIdToDestroy(windowIdToRemove);
    }
}

