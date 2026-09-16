@AroundInvoke
public Object addParameterList(InvocationContext invocationContext) throws Exception {
    return navigationParameterStrategy.execute(invocationContext);
}