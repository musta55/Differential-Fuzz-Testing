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
        String result = formatSampleNames(sampleNames);
        log.info("Exported transactions: jmeter.reportgenerator.exporter.html.series_filter=^({})(-success|-failure)?$", result);
        showResult(e, "jmeter.reportgenerator.exporter.html.series_filter=^(" + result + ")(-success|-failure)?$");
    }
}
// ---- helper method(s) introduced by the refactoring ----
private static String formatSampleNames(Set<String> sampleNames) {
    StringBuilder builder = new StringBuilder();
    for (String sampleName : sampleNames) {
        builder.append(sampleName).append('|');
    }
    builder.setLength(builder.length() - 1);
    return builder.toString();
}

private static JMenuItem createMenuItem(String actionName) {
    JMenuItem menuItemIC = new JMenuItem(JMeterUtils.getResString(actionName), KeyEvent.VK_UNDEFINED);
    menuItemIC.setName(actionName);
    menuItemIC.setActionCommand(actionName);
    menuItemIC.setAccelerator(null);
    menuItemIC.addActionListener(ActionRouter.getInstance());
    return menuItemIC;
}

