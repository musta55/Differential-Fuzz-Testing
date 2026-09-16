@SuppressWarnings("unchecked")
public Object executeQuery(CdiQueryInvocationContext context) {
    Object result = execute(context);
    if (!isUnmappableResult(result) && context.hasQueryInOutMapper()) {
        QueryInOutMapper<Object> mapper = (QueryInOutMapper<Object>) context.getQueryInOutMapper();
        if (result instanceof List) {
            return mapper.mapResultList((List<Object>) result);
        }
        return mapper.mapResult(result);
    }
    return result;
}