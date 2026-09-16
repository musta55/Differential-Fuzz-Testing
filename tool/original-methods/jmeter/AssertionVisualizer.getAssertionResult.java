private static String getAssertionResult(SampleResult res) {
    if (res != null) {
        StringBuilder display = new StringBuilder();
        AssertionResult[] assertionResults = res.getAssertionResults();
        for (AssertionResult item : assertionResults) {
            if (item.isFailure() || item.isError()) {
                // $NON-NLS-1$
                display.append("\n\t");
                // $NON-NLS-1$
                display.append(item.getName() != null ? item.getName() + " : " : "");
                display.append(item.getFailureMessage());
            }
        }
        return display.toString();
    }
    return "";
}