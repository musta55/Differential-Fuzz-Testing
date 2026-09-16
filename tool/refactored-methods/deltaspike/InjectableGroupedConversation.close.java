@Override
public void close() {
    Annotation[] qualifiersArray = this.conversationKey.getQualifiers().toArray(new Annotation[0]);
    this.conversationManager.closeConversation(this.conversationKey.getConversationGroup(), qualifiersArray);
}