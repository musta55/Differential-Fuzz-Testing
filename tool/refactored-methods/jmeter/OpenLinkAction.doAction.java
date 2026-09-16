/**
 * @see org.apache.jmeter.gui.action.Command#doAction(ActionEvent)
 */
@Override
public void doAction(ActionEvent e) {
    String url = LINK_MAP.get(e.getActionCommand());
    if (url == null) {
        log.warn("Action {} not handled by this class", e.getActionCommand());
        return;
    }
    try {
        url = appendAnchorIfPresent(url, e.getSource());
        openBrowser(url);
    } catch (Exception err) {
        handleBrowserException(url, err);
    }
}
// ---- helper method(s) introduced by the refactoring ----
private static String appendAnchorIfPresent(String url, Object source) {
    if (source instanceof String[]) {
        url += "#" + ((String[]) source)[1];
    }
    return url;
}

private static void openBrowser(String url) throws IOException, URISyntaxException {
    java.awt.Desktop.getDesktop().browse(new URI(url));
}

private static void handleBrowserException(String url, Exception err) {
    if (err instanceof IOException) {
        log.error("OpenLinkAction: User default browser is not found, or it fails to be launched, or the default handler application failed to be launched on {}", url, err);
    } else if (err instanceof UnsupportedOperationException) {
        log.error("OpenLinkAction: Current platform does not support the Desktop.Action.BROWSE action on {}", url, err);
        showBrowserWarning(url);
    } else if (err instanceof SecurityException) {
        log.error("OpenLinkAction: Security problem on {}", url, err);
    } else {
        log.error("OpenLinkAction on {}", url, err);
    }
}

