public static void executeViewControllerCallback(ViewConfigDescriptor viewDefinitionEntry, Class<? extends Annotation> callbackType) {
    if (viewDefinitionEntry == null) {
        return;
    }
    SimpleCallbackDescriptor initViewCallbackDescriptor = viewDefinitionEntry.getExecutableCallbackDescriptor(ViewControllerRef.class, callbackType, SimpleCallbackDescriptor.class);
    if (initViewCallbackDescriptor != null) {
        initViewCallbackDescriptor.execute();
    }
}