public synchronized /*no issue due to session-scoped instance*/
void checkWindowContextQuota(String windowId) {
    if (windowId == null) {
        return;
    }
    if (this.quotaHandlerCache.cacheWindowId(windowId)) {
        return;
    }
    /*
         * the following part gets executed only once per request, if the window-id is the same
         */
    if (this.windowIdStack.contains(windowId)) {
        if (//don't move it up if there is just one entry
        windowIdStack.size() > 1) {
            this.windowIdStack.remove(windowId);
            this.windowIdStack.push(windowId);
        }
    } else {
        this.windowIdStack.push(windowId);
        if (this.windowIdStack.size() > this.maxWindowContextCount) {
            String windowIdToRemove = this.windowIdStack.remove(0);
            //destroy it lazily at the end of the request to avoid an overhead during the request
            //which might be caused by pre-destroy logic of window-scoped beans
            this.quotaHandlerCache.setWindowIdToDestroy(windowIdToRemove);
        }
    }
}