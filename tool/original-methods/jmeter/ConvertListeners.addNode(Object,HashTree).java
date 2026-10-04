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
            if (item instanceof RemoteThreadsListenerTestElement) {
                // Used for remote notification of threads start/stop,see BUG 54152
                try {
                    RemoteThreadsListenerWrapper wrapper = new RemoteThreadsListenerWrapper(new RemoteThreadsListenerImpl());
                    subTree.replaceKey(item, wrapper);
                } catch (RemoteException e) {
                    log.error("Error replacing {} by wrapper: {}", RemoteThreadsListenerTestElement.class, RemoteThreadsListenerWrapper.class, e);
                }
                continue;
            }
            if (item instanceof ThreadListener) {
                // TODO Document the reason for this
                log.error("Cannot handle ThreadListener Remotable item: {}", item.getClass());
                continue;
            }
            try {
                RemoteSampleListener rtl = new RemoteSampleListenerImpl(item);
                if (item instanceof TestStateListener && item instanceof SampleListener) {
                    // TL - all
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
                // $NON-NLS-1$
                log.error("RemoteException occurred while replacing Remotable item.", e);
            }
        }
    }
}