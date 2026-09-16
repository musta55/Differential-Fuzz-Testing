/* (non-Javadoc)
     * @see org.apache.jmeter.visualizers.request.RequestView#setSamplerResult(java.lang.Object)
     */
@Override
public void setSamplerResult(Object objectResult) {
    if (objectResult instanceof SampleResult) {
        SampleResult sampleResult = (SampleResult) objectResult;
        // Don't display Request headers label if rh is null or empty
        String rh = sampleResult.getRequestHeaders();
        if (rh != null && !rh.isEmpty()) {
            headerData.setInitialText(rh);
            sampleDataField.setCaretPosition(0);
        }
        String data = sampleResult.getSamplerData();
        if (data != null && !data.isEmpty()) {
            sampleDataField.setText(data);
            sampleDataField.setCaretPosition(0);
        } else {
            // add a message when no request data (ex. Java request)
            sampleDataField.setText(JMeterUtils.getResString(//$NON-NLS-1$
            "view_results_table_request_raw_nodata"));
        }
    }
}