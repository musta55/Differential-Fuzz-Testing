@Override
public boolean distribute(Set<PhysicalNode> nodes, SerializedData data) throws InterruptedException {
    PhysicalNode leastBusyNode = null;
    for (PhysicalNode node : nodes) {
        if (leastBusyNode == null || node.getProcessedMessageCount() < leastBusyNode.getProcessedMessageCount()) {
            leastBusyNode = node;
        }
    }
    return leastBusyNode != null && leastBusyNode.send(data);
}