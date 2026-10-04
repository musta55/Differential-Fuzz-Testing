public boolean isEmpty() {
    if (_arr == null)
        return true;
    for (double value : _arr) {
        if (value != 0)
            return false;
    }
    return true;
}