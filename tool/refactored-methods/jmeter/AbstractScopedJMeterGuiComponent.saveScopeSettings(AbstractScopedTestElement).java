/**
 * Save the scope settings in the test element.
 *
 * @param testElement
 *            the test element to save the settings into
 */
protected void saveScopeSettings(AbstractScopedTestElement testElement) {
    if (scopePanel.isScopeParent()) {
        testElement.setScopeParent();
    } else if (scopePanel.isScopeChildren()) {
        testElement.setScopeChildren();
    } else if (scopePanel.isScopeAll()) {
        testElement.setScopeAll();
    } else if (scopePanel.isScopeVariable()) {
        saveVariableScope(testElement);
    } else {
        throw new IllegalArgumentException("Unexpected scope panel state");
    }
}
// ---- helper method(s) introduced by the refactoring ----
private void saveVariableScope(AbstractScopedTestElement testElement) {
    testElement.setScopeVariable(scopePanel.getVariable());
}

private void showVariableScope(AbstractScopedTestElement testElement) {
    scopePanel.setScopeVariable(testElement.getVariableName());
}

