@Override
public boolean distribute(Set<PhysicalNode> nodes, SerializedData data) throws InterruptedException {
    int size = nodes.size();
    /*
     * why do i need to do this check? synchronization issues? because if there is no one interested,
     * the logical group should not exist!
     */
    if (size > 0) {
        index %= size;
        int count = index++;
        /*
       * May need to look at accessing nodes as arrays, so that iteration can be avoided
       * This matters if say there are 1000+ partitions(?) and this may happen in a Big Message
       * application
       *
       */
        for (PhysicalNode node : nodes) {
            if (count-- == 0) {
                return node.send(data);
            }
        }
    }
    return false;
}