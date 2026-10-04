private void createScene(final String htmlContent) {
    Platform.setImplicitExit(false);
    Platform.runLater(() -> {
        WebView view = new WebView();
        engine = view.getEngine();
        engine.setOnStatusChanged(event -> SwingUtilities.invokeLater(() -> lblStatus.setText(event.getData())));
        engine.getLoadWorker().workDoneProperty().addListener((ChangeListener<Number>) (observableValue, oldValue, newValue) -> SwingUtilities.invokeLater(() -> progressBar.setValue(newValue.intValue())));
        engine.getLoadWorker().exceptionProperty().addListener((ObservableValue<? extends Throwable> o, Throwable old, final Throwable value) -> {
            if (engine.getLoadWorker().getState() == State.FAILED) {
                SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(resultsScrollPane, (value != null) ? engine.getLocation() + "\n" + value.getMessage() : engine.getLocation() + "\nUnexpected error.", "Loading error...", JOptionPane.ERROR_MESSAGE));
            }
        });
        jfxPanel.setScene(new Scene(view));
    });
}