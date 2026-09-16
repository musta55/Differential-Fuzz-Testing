/**
 * {@inheritDoc}
 */
@Override
public void addNode(Object node, HashTree subTree) {
    for (Object item : subTree.list()) {
        if (item instanceof AbstractThreadGroup && log.isDebugEnabled()) {
            log.debug("num threads = {}", ((AbstractThreadGroup) item).getNumThreads());
        }
        if (item instanceof Remoteable) {
            handleRemoteThreadsListenerTestElement(item, subTree);
            handleThreadListener(item);
            handleOtherRemotableItems(item, subTree);
        }
    }
}
// ---- helper method(s) introduced by the refactoring ----
private static void handleRemoteThreadsListenerTestElement(Object item, HashTree subTree) {
    if (item instanceof RemoteThreadsListenerTestElement) {
        try {
            RemoteThreadsListenerWrapper wrapper = new RemoteThreadsListenerWrapper(new RemoteThreadsListenerImpl());
            subTree.replaceKey(item, wrapper);
        } catch (RemoteException e) {
            log.error("Error replacing {} by wrapper: {}", RemoteThreadsListenerTestElement.class, RemoteThreadsListenerWrapper.class, e);
        }
    }
}

private static void handleThreadListener(Object item) {
    if (item instanceof ThreadListener) {
        log.error("Cannot handle ThreadListener Remotable item: {}", item.getClass());
    }
}

private static void handleOtherRemotableItems(Object item, HashTree subTree) {
    if (!(item instanceof RemoteThreadsListenerTestElement) && !(item instanceof ThreadListener)) {
        try {
            RemoteSampleListener rtl = new RemoteSampleListenerImpl(item);
            if (item instanceof TestStateListener && item instanceof SampleListener) {
                RemoteListenerWrapper wrap = new RemoteListenerWrapper(rtl);
                subTree.replaceKey(item, wrap);
            } else if (item instanceof TestStateListener) {
                RemoteTestListenerWrapper wrap = new RemoteTestListenerWrapper(rtl);
                subTree.replaceKey(item, wrap);
            } else if (item instanceof SampleListener) {
                RemoteSampleListenerWrapper wrap = new RemoteSampleListenerWrapper(rtl);
                subTree.replaceKey(item, wrap);
            } else {
                if (log.isWarnEnabled()) {
                    log.warn("Could not replace Remotable item: {}", item.getClass());
                }
            }
        } catch (RemoteException e) {
            log.error("RemoteException occurred while replacing Remotable item.", e);
        }
    }
}

