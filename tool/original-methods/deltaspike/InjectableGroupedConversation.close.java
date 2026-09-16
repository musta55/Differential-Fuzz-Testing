@Override
public void close() {
    Set<Annotation> qualifiers = this.conversationKey.getQualifiers();
    this.conversationManager.closeConversation(this.conversationKey.getConversationGroup(), qualifiers.toArray(new Annotation[qualifiers.size()]));
}