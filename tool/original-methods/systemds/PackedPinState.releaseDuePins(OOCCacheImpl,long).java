long releaseDuePins(OOCCacheImpl physical, long nowNanos) {
    ArrayList<PackedRelease> due = null;
    long nextDueNanos = Long.MAX_VALUE;
    synchronized (this) {
        for (int i = 0; i < _size; ) {
            PackedUnpinHandle handle = _releaseHandles[i];
            if (handle == null || _counts[i] > 0) {
                i++;
                continue;
            }
            long dueNanos = _releaseDueNanos[i];
            if (dueNanos > nowNanos) {
                nextDueNanos = Math.min(nextDueNanos, dueNanos);
                i++;
                continue;
            }
            if (due == null)
                due = new ArrayList<>();
            due.add(new PackedRelease(_allowances[i], handle));
            removeAt(i);
        }
    }
    if (due != null)
        for (PackedRelease release : due) releasePhysicalPin(physical, release.allowance, release.handle);
    return nextDueNanos;
}