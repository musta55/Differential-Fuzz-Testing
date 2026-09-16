public static void executeViewControllerCallback(ViewConfigDescriptor viewDefinitionEntry, Class<? extends Annotation> callbackType) {
    if (viewDefinitionEntry == null) {
        return;
    }
    SimpleCallbackDescriptor callbackDescriptor = getCallbackDescriptor(viewDefinitionEntry, callbackType);
    executeCallback(callbackDescriptor);
}
// ---- helper method(s) introduced by the refactoring ----
private static SimpleCallbackDescriptor getCallbackDescriptor(ViewConfigDescriptor viewDefinitionEntry, Class<? extends Annotation> callbackType) {
    return viewDefinitionEntry.getExecutableCallbackDescriptor(ViewControllerRef.class, callbackType, SimpleCallbackDescriptor.class);
}

private static void executeCallback(SimpleCallbackDescriptor callbackDescriptor) {
    if (callbackDescriptor != null) {
        callbackDescriptor.execute();
    }
}

