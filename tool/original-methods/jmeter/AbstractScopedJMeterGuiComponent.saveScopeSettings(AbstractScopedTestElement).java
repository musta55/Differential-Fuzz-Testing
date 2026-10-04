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
        testElement.setScopeVariable(scopePanel.getVariable());
    } else {
        throw new IllegalArgumentException("Unexpected scope panel state");
    }
}