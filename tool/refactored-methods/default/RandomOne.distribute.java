@Override
public boolean distribute(Set<PhysicalNode> nodes, SerializedData data) throws InterruptedException {
    List<PhysicalNode> nodeList = new ArrayList<>(nodes);
    int count = (int) (Math.random() * nodeList.size());
    /*
     * Should look at accessing nodes within the Set as array. Will save iteration through all the
     * physical nodes.
     *
     */
    PhysicalNode node = nodeList.get(count);
    return node.send(data);
}