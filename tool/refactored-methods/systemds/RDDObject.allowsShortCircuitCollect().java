public boolean allowsShortCircuitCollect() {
    if (isInLineageCache())
        return false;
    return isCheckpointRDDWithSingleRDDChild();
}
// ---- helper method(s) introduced by the refactoring ----
private boolean shouldNotTrustHDFSFile() {
    return isInLineageCache() && isCheckpointRDD();
}

private boolean isCheckpointRDDWithSingleHDFSChild() {
    return isCheckpointRDD() && getLineageChilds().size() == 1;
}

private boolean isCheckpointRDDWithSingleRDDChild() {
    return isCheckpointRDD() && getLineageChilds().size() == 1 && getLineageChilds().get(0) instanceof RDDObject;
}

private boolean isCheckpointed() {
    return _checkpointed;
}

private boolean hasCheckpointRDDChildRecursively() {
    for (LineageObject lo : getLineageChilds()) {
        if (lo instanceof RDDObject && ((RDDObject) lo).rHasCheckpointRDDChilds()) {
            return true;
        }
    }
    return false;
}

