public static void describe(GenericOperator operator, OperatorDescriptor descriptor) {
    for (Class<?> c = operator.getClass(); c != Object.class; c = c.getSuperclass()) {
        Field[] fields = c.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            InputPortFieldAnnotation inputAnnotation = field.getAnnotation(InputPortFieldAnnotation.class);
            OutputPortFieldAnnotation outputAnnotation = field.getAnnotation(OutputPortFieldAnnotation.class);
            AppData.QueryPort adqAnnotation = field.getAnnotation(AppData.QueryPort.class);
            AppData.ResultPort adrAnnotation = field.getAnnotation(AppData.ResultPort.class);
            try {
                Object portObject = field.get(operator);
                if (portObject instanceof InputPort) {
                    descriptor.addInputPort((Operator.InputPort<?>) portObject, field, inputAnnotation, adqAnnotation);
                } else {
                    if (inputAnnotation != null) {
                        throw new IllegalArgumentException("port is not of type " + InputPort.class.getName() + ": " + field);
                    }
                }
                if (portObject instanceof OutputPort) {
                    descriptor.addOutputPort((Operator.OutputPort<?>) portObject, field, outputAnnotation, adrAnnotation);
                } else {
                    if (outputAnnotation != null) {
                        throw new IllegalArgumentException("port is not of type " + OutputPort.class.getName() + ": " + field);
                    }
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
    }
}