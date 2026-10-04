public void findEvictionCandidates(MaskedOnceArrayList<BlockEntry> list, PriorityQueue<IndexedObjectPair<BlockEntry>> candidates, int k, long estimatedReuseTimestamp) {
    if (_op.isEmpty()) {
        list.forEachLive((idx, b) -> {
            if (!isEvictionCandidate(b))
                return true;
            var iop = new IndexedObjectPair<>(estimatedReuseTimestamp + idx, b);
            if (candidates.size() < k) {
                candidates.offer(iop);
            } else if (iop.compareTo(candidates.peek()) > 0) {
                candidates.poll();
                candidates.offer(iop);
            }
            return true;
        }, true);
        return;
    }
    list.forEachLive((idx, b) -> {
        if (!isEvictionCandidate(b))
            return true;
        long score = computeScore(idx);
        if (score == Long.MAX_VALUE)
            score = idx + estimatedReuseTimestamp;
        var iop = new IndexedObjectPair<>(score, b);
        if (candidates.size() < k) {
            candidates.offer(iop);
        } else if (iop.compareTo(candidates.peek()) > 0) {
            candidates.poll();
            candidates.offer(iop);
        }
        return true;
    }, true);
}