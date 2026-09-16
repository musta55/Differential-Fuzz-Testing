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
        showVariableScope(testElement);
    } else {
        throw new IllegalArgumentException("Invalid scope: " + scope);
    }
}
// ---- helper method(s) introduced by the refactoring ----
private void saveVariableScope(AbstractScopedTestElement testElement) {
    testElement.setScopeVariable(scopePanel.getVariable());
}

private void showVariableScope(AbstractScopedTestElement testElement) {
    scopePanel.setScopeVariable(testElement.getVariableName());
}

