public static void describe(GenericOperator operator, OperatorDescriptor descriptor) {
    for (Class<?> c = operator.getClass(); c != Object.class; c = c.getSuperclass()) {
        Field[] fields = c.getDeclaredFields();
        for (Field field : fields) {
            processField(operator, descriptor, field);
        }
    }
}
// ---- helper method(s) introduced by the refactoring ----
private static void processField(GenericOperator operator, OperatorDescriptor descriptor, Field field) {
    field.setAccessible(true);
    InputPortFieldAnnotation inputAnnotation = field.getAnnotation(InputPortFieldAnnotation.class);
    OutputPortFieldAnnotation outputAnnotation = field.getAnnotation(OutputPortFieldAnnotation.class);
    AppData.QueryPort adqAnnotation = field.getAnnotation(AppData.QueryPort.class);
    AppData.ResultPort adrAnnotation = field.getAnnotation(AppData.ResultPort.class);
    try {
        Object portObject = field.get(operator);
        processInputPort(descriptor, field, inputAnnotation, adqAnnotation, portObject);
        processOutputPort(descriptor, field, outputAnnotation, adrAnnotation, portObject);
    } catch (IllegalAccessException e) {
        throw new RuntimeException(e);
    }
}

private static void processInputPort(OperatorDescriptor descriptor, Field field, InputPortFieldAnnotation inputAnnotation, AppData.QueryPort adqAnnotation, Object portObject) {
    if (portObject instanceof InputPort) {
        descriptor.addInputPort((InputPort<?>) portObject, field, inputAnnotation, adqAnnotation);
    } else if (inputAnnotation != null) {
        throw new IllegalArgumentException("port is not of type " + InputPort.class.getName() + ": " + field);
    }
}

private static void processOutputPort(OperatorDescriptor descriptor, Field field, OutputPortFieldAnnotation outputAnnotation, AppData.ResultPort adrAnnotation, Object portObject) {
    if (portObject instanceof OutputPort) {
        descriptor.addOutputPort((OutputPort<?>) portObject, field, outputAnnotation, adrAnnotation);
    } else if (outputAnnotation != null) {
        throw new IllegalArgumentException("port is not of type " + OutputPort.class.getName() + ": " + field);
    }
}

