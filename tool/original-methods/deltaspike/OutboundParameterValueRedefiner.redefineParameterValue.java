/**
 * {@inheritDoc}
 */
@Override
public Object redefineParameterValue(ParameterValue value) {
    CreationalContext<?> ctx = BeanManagerProvider.getInstance().getBeanManager().createCreationalContext(declaringBean);
    try {
        if (value.getPosition() == handlerMethod.getHandlerParameter().getPosition()) {
            return event;
        }
        return value.getDefaultValue(ctx);
    } finally {
        if (ctx != null) {
            ctx.release();
        }
    }
}