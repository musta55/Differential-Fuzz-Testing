private <T extends CallbackDescriptor> T findCallbackDescriptor(Class<? extends Annotation> metaDataType, Class<? extends Annotation> callbackType) {
    List<CallbackDescriptor> foundDescriptors = callbackDescriptors.get(metaDataType);
    if (foundDescriptors == null || foundDescriptors.isEmpty()) {
        return null;
    }
    if (callbackType == null || DefaultCallback.class.equals(callbackType)) {
        if (foundDescriptors.size() > 1) {
            //TODO validate during bootstrapping
            throw new IllegalStateException("multiple descriptors for " + ((callbackType == null) ? DefaultCallback.class.getName() : callbackType.getName()) + " aren't allowed");
        }
        return (T) foundDescriptors.iterator().next();
    }
    for (CallbackDescriptor callbackDescriptor : foundDescriptors) {
        if (callbackDescriptor.isBoundTo(callbackType)) {
            return (T) callbackDescriptor;
        }
    }
    return null;
}