@Override
protected CompressedSizeInfo coCodeColumns(CompressedSizeInfo colInfos, int k) {
    final int startSize = colInfos.getInfo().size();
    final int pqColumnThreshold = Math.max(128, (_sest.getNumColumns() / startSize) * 100);
    if (startSize == 1)
        // nothing to join when there only is one column
        return colInfos;
    if (startSize <= 16)
        return handleFewColumns(colInfos, k);
    if (startSize > 1000)
        return handleManyColumns(colInfos, k, pqColumnThreshold);
    return handleHybridStrategy(colInfos, k, pqColumnThreshold);
}
// ---- helper method(s) introduced by the refactoring ----
private CompressedSizeInfo handleFewColumns(CompressedSizeInfo colInfos, int k) {
    if (LOG.isDebugEnabled())
        LOG.debug("Hybrid chose to do greedy CoCode because of few columns");
    CoCodeGreedy gd = new CoCodeGreedy(_sest, _cest, _cs);
    return colInfos.setInfo(gd.combine(colInfos.getInfo(), k));
}

private CompressedSizeInfo handleManyColumns(CompressedSizeInfo colInfos, int k, int pqColumnThreshold) {
    CoCodePriorityQue pq = new CoCodePriorityQue(_sest, _cest, _cs, pqColumnThreshold);
    return colInfos.setInfo(pq.join(colInfos.getInfo(), 1, k));
}

private CompressedSizeInfo handleHybridStrategy(CompressedSizeInfo colInfos, int k, int pqColumnThreshold) {
    if (LOG.isDebugEnabled())
        LOG.debug("Using Hybrid CoCode Strategy: ");
    final int priorityQueueGoal = colInfos.getInfo().size() / 5;
    if (priorityQueueGoal <= 30)
        return handleNotLargeEnoughColumns(colInfos, k);
    return applyHybridLogic(colInfos, k, pqColumnThreshold, priorityQueueGoal);
}

private CompressedSizeInfo handleNotLargeEnoughColumns(CompressedSizeInfo colInfos, int k) {
    if (LOG.isDebugEnabled())
        LOG.debug("Using only Greedy based since Nr Column groups: " + colInfos.getInfo().size() + " is not large enough");
    CoCodeGreedy gd = new CoCodeGreedy(_sest, _cest, _cs);
    return colInfos.setInfo(gd.combine(colInfos.getInfo(), k));
}

private CompressedSizeInfo applyHybridLogic(CompressedSizeInfo colInfos, int k, int pqColumnThreshold, int priorityQueueGoal) {
    Timing time = new Timing(true);
    CoCodePriorityQue pq = new CoCodePriorityQue(_sest, _cest, _cs, pqColumnThreshold);
    colInfos.setInfo(pq.join(colInfos.getInfo(), priorityQueueGoal, k));
    final int pqSize = colInfos.getInfo().size();
    if (LOG.isDebugEnabled())
        LOG.debug("Que based time: " + time.stop());
    if (pqSize < priorityQueueGoal || (pqSize < colInfos.getInfo().size() && _cest instanceof ComputationCostEstimator))
        return applyGreedyAfterPriorityQueue(colInfos, k, time);
    return colInfos;
}

private CompressedSizeInfo applyGreedyAfterPriorityQueue(CompressedSizeInfo colInfos, int k, Timing time) {
    CoCodeGreedy gd = new CoCodeGreedy(_sest, _cest, _cs);
    colInfos.setInfo(gd.combine(colInfos.getInfo(), k));
    if (LOG.isDebugEnabled())
        LOG.debug("Greedy time:     " + time.stop());
    return colInfos;
}

