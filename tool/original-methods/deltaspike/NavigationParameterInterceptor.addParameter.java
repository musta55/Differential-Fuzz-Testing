@AroundInvoke
public Object addParameter(InvocationContext invocationContext) throws Exception {
    return this.navigationParameterStrategy.execute(invocationContext);
}