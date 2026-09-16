private GroupedConversationManager getConversationManager() {
    if (conversationManager == null) {
        conversationManager = BeanProvider.getContextualReference(DeltaSpikeContextExtension.class).getConversationContext();
    }
    return conversationManager;
}