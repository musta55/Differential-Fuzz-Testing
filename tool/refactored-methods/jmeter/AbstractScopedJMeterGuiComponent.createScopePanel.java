/**
 * Create the scope settings panel.
 *
 * @return the scope settings panel
 */
protected JPanel createScopePanel() {
    return createScopePanel(false, true, true);
}
// ---- helper method(s) introduced by the refactoring ----
private void saveVariableScope(AbstractScopedTestElement testElement) {
    testElement.setScopeVariable(scopePanel.getVariable());
}

private void showVariableScope(AbstractScopedTestElement testElement) {
    scopePanel.setScopeVariable(testElement.getVariableName());
}

