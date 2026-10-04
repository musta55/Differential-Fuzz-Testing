public boolean rHasCheckpointRDDChilds() {
    //probe for checkpoint rdd
    if (_checkpointed)
        return true;
    //process childs recursively
    boolean ret = false;
    for (LineageObject lo : getLineageChilds()) {
        if (lo instanceof RDDObject)
            ret |= ((RDDObject) lo).rHasCheckpointRDDChilds();
    }
    return ret;
}