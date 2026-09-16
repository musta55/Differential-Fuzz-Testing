private GroupedConversationManager getConversationManager() {
    if (this.conversationManager == null) {
        this.conversationManager = BeanProvider.getContextualReference(DeltaSpikeContextExtension.class).getConversationContext();
    }
    return conversationManager;
}