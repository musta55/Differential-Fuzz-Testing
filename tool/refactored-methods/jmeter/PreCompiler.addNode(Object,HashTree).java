/**
 * {@inheritDoc}
 */
@Override
public void addNode(Object node, HashTree subTree) {
    if (isClientSide) {
        handleClientSideNode(node);
    } else {
        handleServerSideNode(node);
    }
}
// ---- helper method(s) introduced by the refactoring ----
private void handleClientSideNode(Object node) {
    if (node instanceof ResultCollector || node instanceof Backend) {
        replaceNodeValues(node);
    }
    if (node instanceof TestPlan) {
        this.clientSideVariables = createVars((TestPlan) node);
    }
    if (node instanceof Arguments) {
        Map<String, String> args = createArgumentsMap((Arguments) node);
        clientSideVariables.putAll(args);
    }
}

private void handleServerSideNode(Object node) {
    if (node instanceof TestElement) {
        replaceNodeValues(node);
    }
    if (node instanceof TestPlan) {
        JMeterVariables vars = createVars((TestPlan) node);
        JMeterContextService.getContext().setVariables(vars);
    }
    if (node instanceof Arguments) {
        Map<String, String> args = createArgumentsMap((Arguments) node);
        JMeterContextService.getContext().getVariables().putAll(args);
    }
}

private void replaceNodeValues(Object node) {
    try {
        replacer.replaceValues((TestElement) node);
    } catch (InvalidVariableException e) {
        log.error("invalid variables in node {}", ((TestElement) node).getName(), e);
    }
}

