private void createScene(final String htmlContent) {
    Platform.setImplicitExit(false);
    Platform.runLater(() -> {
        WebView view = new WebView();
        engine = view.getEngine();
        engine.setOnStatusChanged(event -> SwingUtilities.invokeLater(() -> lblStatus.setText(event.getData())));
        engine.getLoadWorker().workDoneProperty().addListener((ChangeListener<Number>) (ov, oldValue, newValue) -> SwingUtilities.invokeLater(() -> progressBar.setValue(newValue.intValue())));
        engine.getLoadWorker().exceptionProperty().addListener((o, old, value) -> {
            if (engine.getLoadWorker().getState() == State.FAILED) {
                SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(resultsScrollPane, (value != null) ? engine.getLocation() + "\n" + value.getMessage() : engine.getLocation() + "\nUnexpected error.", "Loading error...", JOptionPane.ERROR_MESSAGE));
            }
        });
        jfxPanel.setScene(new Scene(view));
    });
}
// ---- helper method(s) introduced by the refactoring ----
private String extractHtmlContent(String response) {
    int htmlIndex = response.indexOf("<HTML");
    if (htmlIndex < 0) {
        htmlIndex = response.indexOf("<html");
    }
    if (htmlIndex < 0) {
        htmlIndex = 0;
    }
    return response.substring(htmlIndex);
}

