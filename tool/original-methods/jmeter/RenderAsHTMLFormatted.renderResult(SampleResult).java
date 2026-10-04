/**
 * {@inheritDoc}
 */
@Override
public void renderResult(SampleResult sampleResult) {
    String response = ViewResultsFullVisualizer.getResponseAsString(sampleResult);
    showHTMLFormattedResponse(response);
}