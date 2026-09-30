@Override
public boolean distribute(Set<PhysicalNode> nodes, SerializedData data) throws InterruptedException {
    if (nodes.isEmpty()) {
        return false;
    }
    nodeList.clear();
    nodeList.addAll(nodes);
    int size = nodeList.size();
    index %= size;
    PhysicalNode node = nodeList.get(index++);
    return node.send(data);
}