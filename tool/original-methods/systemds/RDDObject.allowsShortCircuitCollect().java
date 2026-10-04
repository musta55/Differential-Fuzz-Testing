public boolean allowsShortCircuitCollect() {
    // If the RDD is marked to be persisted and cached locally, we want to collect the RDD
    // so that the next time we can reuse the RDD.
    if (isInLineageCache())
        return false;
    return (isCheckpointRDD() && getLineageChilds().size() == 1 && getLineageChilds().get(0) instanceof RDDObject);
}