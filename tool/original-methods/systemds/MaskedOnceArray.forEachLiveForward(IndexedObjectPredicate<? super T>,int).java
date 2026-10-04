private boolean forEachLiveForward(IndexedObjectPredicate<? super T> action, int offset) {
    int len = _liveState.length();
    T data;
    for (int word = 0; word < len; word++) {
        if (_liveState.getWord(word) == 0)
            continue;
        int lower = word * 64;
        int upper = (word + 1) * 64;
        for (int i = lower; i < upper; i++) {
            data = get(i);
            if (data != null)
                if (!action.test(offset + i, data))
                    return false;
        }
    }
    return true;
}