@Override
public boolean distribute(Set<PhysicalNode> nodes, SerializedData data) throws InterruptedException {
    PhysicalNode theOne = null;
    for (PhysicalNode node : nodes) {
        if (theOne == null || node.getProcessedMessageCount() < theOne.getProcessedMessageCount()) {
            theOne = node;
        }
    }
    return theOne == null ? false : theOne.send(data);
}