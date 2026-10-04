protected static void rCleanupChildRDDs(LineageObject lob) {
    // Abort recursive cleanup if still consumers
    if (lob.getNumReferences() > 0)
        return;
    // Abort if still reachable through live matrix object
    if (lob.hasBackReference())
        return;
    // Abort if the RDD is yet to be persisted
    if (lob instanceof RDDObject && lob.isInLineageCache() && SparkExecutionContext.isRDDCached(((RDDObject) lob).getRDD().id()))
        return;
    // Cleanup current lineage object (from driver/executors)
    SparkExecutionContext.cleanupSingleLineageObject(lob);
    //recursively process lineage children
    for (LineageObject c : lob.getLineageChilds()) {
        c.decrementNumReferences();
        rCleanupChildRDDs(c);
    }
}