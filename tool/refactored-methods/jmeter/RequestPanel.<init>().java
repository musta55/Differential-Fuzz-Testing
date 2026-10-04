/**
 * Find and instantiate all classes that extend RequestView
 * and Create Request Panel
 */
public RequestPanel() {
    listRequestView = new ArrayDeque<>();
    loadAndInitializeRequestViews();
    panel = createRequestPanel();
}
// ---- helper method(s) introduced by the refactoring ----
private void loadAndInitializeRequestViews() {
    // $NON-NLS-1$
    String rawTab = JMeterUtils.getResString(RequestViewRaw.KEY_LABEL);
    RequestView rawObject = null;
    for (RequestView requestView : JMeterUtils.loadServicesAndScanJars(RequestView.class, ServiceLoader.load(RequestView.class), Thread.currentThread().getContextClassLoader(), new LogAndIgnoreServiceLoadExceptionHandler(log))) {
        if (rawTab.equals(requestView.getLabel())) {
            // use later
            rawObject = requestView;
        } else {
            listRequestView.add(requestView);
        }
    }
    // place raw tab in first position (first tab)
    if (rawObject != null) {
        listRequestView.addFirst(rawObject);
    }
}

private JPanel createRequestPanel() {
    JTabbedPane tabbedRequest = new JTabbedPane(SwingConstants.BOTTOM);
    for (RequestView requestView : listRequestView) {
        requestView.init();
        tabbedRequest.addTab(requestView.getLabel(), requestView.getPanel());
    }
    // Hint to background color on bottom tabs (grey, not blue)
    JPanel panel = new JPanel(new BorderLayout());
    panel.add(tabbedRequest);
    return panel;
}

