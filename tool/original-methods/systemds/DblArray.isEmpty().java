public boolean isEmpty() {
    if (_arr == null)
        return true;
    for (int i = 0; i < _arr.length; i++) {
        if (_arr[i] != 0)
            return false;
    }
    return true;
}