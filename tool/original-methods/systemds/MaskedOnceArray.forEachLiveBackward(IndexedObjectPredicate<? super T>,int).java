private boolean forEachLiveBackward(IndexedObjectPredicate<? super T> action, int offset) {
    int len = _liveState.length();
    for (int word = len - 1; word >= 0; word--) {
        if (_liveState.getWord(word) == 0)
            continue;
        int lower = word * 64;
        int upper = (word + 1) * 64;
        T data;
        for (int i = upper - 1; i >= lower; i--) {
            data = get(i);
            if (data != null)
                if (!action.test(offset + i, data))
                    return false;
        }
    }
    return true;
}