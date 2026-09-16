/**
 * End the TransactionScope with the given qualifier.
 * This will subsequently destroy all beans which are stored
 * in the context.
 *
 * This method only gets used if we leave a transaction with REQUIRES_NEW.
 */
public void endTransactionScope() {
    if (LOGGER.isLoggable(Level.FINER)) {
        LOGGER.finer("ending TransactionScope");
    }
    while (!oldTci.isEmpty()) {
        destroyBeans(currentTci.contextualInstances);
        currentTci = oldTci.pop();
    }
    destroyBeans(currentTci.contextualInstances);
    currentTci = null;
}