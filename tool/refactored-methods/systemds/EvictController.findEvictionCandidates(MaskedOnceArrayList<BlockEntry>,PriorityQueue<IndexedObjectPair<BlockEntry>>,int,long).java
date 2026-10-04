public void findEvictionCandidates(MaskedOnceArrayList<BlockEntry> list, PriorityQueue<IndexedObjectPair<BlockEntry>> candidates, int k, long estimatedReuseTimestamp) {
    list.forEachLive((idx, b) -> {
        if (!isEvictionCandidate(b))
            return true;
        processBlock(candidates, k, estimatedReuseTimestamp, idx, b);
        return true;
    }, true);
}
// ---- helper method(s) introduced by the refactoring ----
private void processBlock(PriorityQueue<IndexedObjectPair<BlockEntry>> candidates, int k, long estimatedReuseTimestamp, int idx, BlockEntry b) {
    long score = _op.isEmpty() ? idx + estimatedReuseTimestamp : computeScore(idx);
    var iop = new IndexedObjectPair<>(score, b);
    if (candidates.size() < k) {
        candidates.offer(iop);
    } else if (iop.compareTo(candidates.peek()) > 0) {
        candidates.poll();
        candidates.offer(iop);
    }
}

