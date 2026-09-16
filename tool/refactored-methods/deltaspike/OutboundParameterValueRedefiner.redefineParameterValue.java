/**
 * {@inheritDoc}
 */
@Override
public Object redefineParameterValue(ParameterValue value) {
    CreationalContext<?> ctx = createCreationalContext();
    try {
        if (value.getPosition() == handlerMethod.getHandlerParameter().getPosition()) {
            return event;
        }
        return value.getDefaultValue(ctx);
    } finally {
        releaseCreationalContext(ctx);
    }
}
// ---- helper method(s) introduced by the refactoring ----
private CreationalContext<?> createCreationalContext() {
    return BeanManagerProvider.getInstance().getBeanManager().createCreationalContext(declaringBean);
}

private void releaseCreationalContext(CreationalContext<?> ctx) {
    if (ctx != null) {
        ctx.release();
    }
}

