/**
 * @see Command#doAction(ActionEvent)
 */
@Override
public void doAction(ActionEvent e) {
    HashTree wholeTree = GuiPackage.getInstance().getTreeModel().getTestPlan();
    SamplerAndTransactionNameVisitor visitor = new SamplerAndTransactionNameVisitor();
    wholeTree.traverse(visitor);
    Set<String> sampleNames = visitor.getListOfTransactions();
    if (sampleNames.isEmpty()) {
        log.warn("No transaction exported using regexp '{}', modify property '{}' to fix this problem", TRANSACTIONS_REGEX_PATTERN, "report_transactions_pattern");
        showResult(e, "No transaction exported using regexp '" + TRANSACTIONS_REGEX_PATTERN + "', modify property 'report_transactions_pattern' to fix this problem");
    } else {
        StringBuilder builder = new StringBuilder();
        for (String sampleName : sampleNames) {
            builder.append(sampleName).append('|');
        }
        builder.setLength(builder.length() - 1);
        String result = builder.toString();
        log.info("Exported transactions: jmeter.reportgenerator.exporter.html.series_filter=^({})(-success|-failure)?$", result);
        showResult(e, "jmeter.reportgenerator.exporter.html.series_filter=^(" + result + ")(-success|-failure)?$");
    }
}