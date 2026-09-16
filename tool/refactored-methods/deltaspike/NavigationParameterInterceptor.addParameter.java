@AroundInvoke
public Object addParameter(InvocationContext invocationContext) throws Exception {
    return navigationParameterStrategy.execute(invocationContext);
}