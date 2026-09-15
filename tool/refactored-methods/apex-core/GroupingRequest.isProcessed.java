/**
 * Checks if request is processed
 * @return isProcessed
 */
public boolean isProcessed() {
    return isDeployListEmpty() && isUndeployListEmpty();
}
// ---- helper method(s) introduced by the refactoring ----
private boolean isDeployListEmpty() {
    return getOperatorsToDeploy().isEmpty();
}

private boolean isUndeployListEmpty() {
    return getOperatorsToUndeploy().isEmpty();
}

