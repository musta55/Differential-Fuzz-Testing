/**
 * Checks if request is processed
 * @return isProcessed
 */
public boolean isProcessed() {
    return (getOperatorsToDeploy().isEmpty() && getOperatorsToUndeploy().isEmpty());
}