@Override
public boolean distribute(Set<PhysicalNode> nodes, SerializedData data) throws InterruptedException {
    int count = (int) (Math.random() * nodes.size());
    /*
     * Should look at accessing nodes within the Set as array. Will save iteration through all the
     * physical nodes.
     *
     */
    for (PhysicalNode node : nodes) {
        if (count-- == 0) {
            return node.send(data);
        }
    }
    return false;
}