/**
 * {@inheritDoc}
 */
@Override
public void addNode(Object node, HashTree subTree) {
    if (isClientSide) {
        if (node instanceof ResultCollector || node instanceof Backend) {
            try {
                replacer.replaceValues((TestElement) node);
            } catch (InvalidVariableException e) {
                log.error("invalid variables in node {}", ((TestElement) node).getName(), e);
            }
        }
        if (node instanceof TestPlan) {
            this.clientSideVariables = createVars((TestPlan) node);
        }
        if (node instanceof Arguments) {
            // Don't store User Defined Variables in the context for client side
            Map<String, String> args = createArgumentsMap((Arguments) node);
            clientSideVariables.putAll(args);
        }
    } else {
        if (node instanceof TestElement) {
            try {
                replacer.replaceValues((TestElement) node);
            } catch (InvalidVariableException e) {
                log.error("invalid variables in node {}", ((TestElement) node).getName(), e);
            }
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
}