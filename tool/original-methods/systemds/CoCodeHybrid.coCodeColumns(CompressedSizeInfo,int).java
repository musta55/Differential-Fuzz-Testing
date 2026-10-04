@Override
protected CompressedSizeInfo coCodeColumns(CompressedSizeInfo colInfos, int k) {
    final int startSize = colInfos.getInfo().size();
    final int pqColumnThreashold = Math.max(128, (_sest.getNumColumns() / startSize) * 100);
    if (startSize == 1)
        // nothing to join when there only is one column
        return colInfos;
    else if (startSize <= 16) {
        // Greedy all compare all if small number of columns
        if (LOG.isDebugEnabled())
            LOG.debug("Hybrid chose to do greedy CoCode because of few columns");
        CoCodeGreedy gd = new CoCodeGreedy(_sest, _cest, _cs);
        return colInfos.setInfo(gd.combine(colInfos.getInfo(), k));
    } else if (startSize > 1000) {
        CoCodePriorityQue pq = new CoCodePriorityQue(_sest, _cest, _cs, pqColumnThreashold);
        return colInfos.setInfo(pq.join(colInfos.getInfo(), 1, k));
    }
    if (LOG.isDebugEnabled())
        LOG.debug("Using Hybrid CoCode Strategy: ");
    final int PriorityQueGoal = startSize / 5;
    if (PriorityQueGoal > 30) {
        // hybrid if there is a large number of columns to begin with
        Timing time = new Timing(true);
        CoCodePriorityQue pq = new CoCodePriorityQue(_sest, _cest, _cs, pqColumnThreashold);
        colInfos.setInfo(pq.join(colInfos.getInfo(), PriorityQueGoal, k));
        final int pqSize = colInfos.getInfo().size();
        if (LOG.isDebugEnabled())
            LOG.debug("Que based time: " + time.stop());
        if (pqSize < PriorityQueGoal || (pqSize < startSize && _cest instanceof ComputationCostEstimator)) {
            CoCodeGreedy gd = new CoCodeGreedy(_sest, _cest, _cs);
            colInfos.setInfo(gd.combine(colInfos.getInfo(), k));
            if (LOG.isDebugEnabled())
                LOG.debug("Greedy time:     " + time.stop());
        }
        return colInfos;
    } else {
        if (LOG.isDebugEnabled())
            LOG.debug("Using only Greedy based since Nr Column groups: " + startSize + " is not large enough");
        CoCodeGreedy gd = new CoCodeGreedy(_sest, _cest, _cs);
        colInfos.setInfo(gd.combine(colInfos.getInfo(), k));
        return colInfos;
    }
}