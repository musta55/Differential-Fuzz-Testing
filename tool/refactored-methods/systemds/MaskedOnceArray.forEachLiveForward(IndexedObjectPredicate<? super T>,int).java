private boolean forEachLiveForward(IndexedObjectPredicate<? super T> action, int offset) {
    int len = _liveState.length();
    for (int word = 0; word < len; word++) {
        if (_liveState.getWord(word) == 0)
            continue;
        int lower = word * 64;
        int upper = (word + 1) * 64;
        if (!processRange(action, offset, lower, upper, true))
            return false;
    }
    return true;
}
// ---- helper method(s) introduced by the refactoring ----
private boolean processRange(IndexedObjectPredicate<? super T> action, int offset, int lower, int upper, boolean forward) {
    T data;
    if (forward) {
        for (int i = lower; i < upper; i++) {
            data = get(i);
            if (data != null && !action.test(offset + i, data))
                return false;
        }
    } else {
        for (int i = upper - 1; i >= lower; i--) {
            data = get(i);
            if (data != null && !action.test(offset + i, data))
                return false;
        }
    }
    return true;
}

