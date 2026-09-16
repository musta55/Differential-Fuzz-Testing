/**
 * TODO comment is no equals!!!
 *
 * @param parameter
 * @return
 */
boolean matches(AuthorizationParameter parameter) {
    if (!type.equals(parameter.type)) {
        return false;
    }
    for (Map.Entry<Class<? extends Annotation>, Map<Method, Object>> bindingEntry : bindings.entrySet()) {
        Map<Method, Object> bindingValues = parameter.bindings.get(bindingEntry.getKey());
        if (bindingValues == null) {
            // annotation is not present
            return false;
        }
        for (Map.Entry<Method, Object> value : bindingEntry.getValue().entrySet()) {
            if (!bindingValues.get(value.getKey()).equals(value.getValue())) {
                return false;
            }
        }
    }
    return true;
}