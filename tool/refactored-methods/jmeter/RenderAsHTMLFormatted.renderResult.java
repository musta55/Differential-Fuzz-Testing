/**
 * {@inheritDoc}
 */
@Override
public void renderResult(SampleResult sampleResult) {
    String response = ViewResultsFullVisualizer.getResponseAsString(sampleResult);
    String htmlContent = parseHTML(response);
    updateUIComponents(htmlContent);
}
// ---- helper method(s) introduced by the refactoring ----
private static String parseHTML(String response) {
    // $NON-NLS-1$
    return response == null ? "" : Jsoup.parse(response).html();
}

private void updateUIComponents(String htmlContent) {
    // $NON-NLS-1$
    results.setContentType("text/plain");
    setTextOptimized(htmlContent);
    results.setCaretPosition(0);
    resultsScrollPane.setViewportView(results);
    // Bug 55111 - Refresh JEditor pane size depending on the presence or absence of scrollbars
    resultsScrollPane.setPreferredSize(resultsScrollPane.getMinimumSize());
    results.revalidate();
}

