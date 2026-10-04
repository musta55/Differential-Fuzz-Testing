/**
 * Indicates if rdd is an hdfs file or a checkpoint over an hdfs file;
 * in both cases, we can directly read the file instead of collecting
 * the given rdd.
 *
 * @return true if rdd is an hdfs file or a checkpoint over an hdfs file
 */
public boolean allowsShortCircuitRead() {
    if (shouldNotTrustHDFSFile())
        return false;
    boolean ret = isHDFSFile();
    if (isCheckpointRDDWithSingleHDFSChild()) {
        RDDObject child = (RDDObject) getLineageChilds().get(0);
        ret = child.isHDFSFile();
    }
    return ret;
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

