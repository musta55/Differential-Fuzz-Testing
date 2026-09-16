@AroundInvoke
public Object addParameterList(InvocationContext invocationContext) throws Exception {
    return this.navigationParameterStrategy.execute(invocationContext);
}