@SuppressWarnings("unchecked")
public Object executeQuery(CdiQueryInvocationContext context) {
    Object result = execute(context);
    if (shouldMapResult(result, context)) {
        QueryInOutMapper<Object> mapper = (QueryInOutMapper<Object>) context.getQueryInOutMapper();
        return mapResult(mapper, result);
    }
    return result;
}
// ---- helper method(s) introduced by the refactoring ----
private boolean shouldMapResult(Object result, CdiQueryInvocationContext context) {
    return !isUnmappableResult(result) && context.hasQueryInOutMapper();
}

private Object mapResult(QueryInOutMapper<Object> mapper, Object result) {
    if (result instanceof List) {
        return mapper.mapResultList((List<Object>) result);
    }
    return mapper.mapResult(result);
}

