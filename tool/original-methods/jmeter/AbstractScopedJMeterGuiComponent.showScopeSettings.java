/**
 * Show the scope settings from the test element with variable scope
 *
 * @param testElement
 *            the test element from which the settings should be shown
 * @param enableVariableButton
 *            set true to enable the variable panel
 */
protected void showScopeSettings(AbstractScopedTestElement testElement, boolean enableVariableButton) {
    String scope = testElement.fetchScope();
    if (testElement.isScopeParent(scope)) {
        scopePanel.setScopeParent(enableVariableButton);
    } else if (testElement.isScopeChildren(scope)) {
        scopePanel.setScopeChildren(enableVariableButton);
    } else if (testElement.isScopeAll(scope)) {
        scopePanel.setScopeAll(enableVariableButton);
    } else if (testElement.isScopeVariable(scope)) {
        scopePanel.setScopeVariable(testElement.getVariableName());
    } else {
        throw new IllegalArgumentException("Invalid scope: " + scope);
    }
}