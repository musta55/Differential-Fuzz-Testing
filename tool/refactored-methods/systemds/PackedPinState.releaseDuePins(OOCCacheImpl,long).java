long releaseDuePins(OOCCacheImpl physical, long nowNanos) {
    ArrayList<PackedRelease> due = findDueReleases(nowNanos);
    long nextDueNanos = processDueReleases(physical, due);
    return nextDueNanos;
}
// ---- helper method(s) introduced by the refactoring ----
private synchronized ArrayList<PackedRelease> findDueReleases(long nowNanos) {
    ArrayList<PackedRelease> due = null;
    long nextDueNanos = Long.MAX_VALUE;
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
    return due;
}

private long processDueReleases(OOCCacheImpl physical, ArrayList<PackedRelease> due) {
    long nextDueNanos = Long.MAX_VALUE;
    if (due != null)
        for (PackedRelease release : due) releasePhysicalPin(physical, release.allowance, release.handle);
    return nextDueNanos;
}

