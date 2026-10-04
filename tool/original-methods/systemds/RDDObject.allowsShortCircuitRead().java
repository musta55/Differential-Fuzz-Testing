/**
 * Indicates if rdd is an hdfs file or a checkpoint over an hdfs file;
 * in both cases, we can directly read the file instead of collecting
 * the given rdd.
 *
 * @return true if rdd is an hdfs file or a checkpoint over an hdfs file
 */
public boolean allowsShortCircuitRead() {
    // Cannot trust the hdfs file for reused RDD objects
    if (isInLineageCache() && isCheckpointRDD())
        return false;
    boolean ret = isHDFSFile();
    if (isCheckpointRDD() && getLineageChilds().size() == 1) {
        LineageObject lo = getLineageChilds().get(0);
        ret = (lo instanceof RDDObject && ((RDDObject) lo).isHDFSFile());
    }
    return ret;
}