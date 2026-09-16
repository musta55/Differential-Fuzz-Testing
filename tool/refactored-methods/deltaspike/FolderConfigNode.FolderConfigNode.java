public FolderConfigNode(ViewConfigNode nodeToCopy, Class<?> viewConfigClass) {
    super(nodeToCopy.getParent(), nodeToCopy.getMetaData());
    getInheritedMetaData().addAll(nodeToCopy.getInheritedMetaData());
    getChildren().addAll(nodeToCopy.getChildren());
    for (Map.Entry<Class<? extends Annotation>, List<CallbackDescriptor>> callbackDescriptorEntry : nodeToCopy.getCallbackDescriptors().entrySet()) {
        for (CallbackDescriptor callbackDescriptor : callbackDescriptorEntry.getValue()) {
            registerCallbackDescriptors(callbackDescriptorEntry.getKey(), callbackDescriptor);
        }
    }
    this.nodeId = viewConfigClass;
}