@Override
public void requestDestroyed(ServletRequestEvent sre) {
    if (activated) {
        RequestResponseHolder.REQUEST.release();
    }
}