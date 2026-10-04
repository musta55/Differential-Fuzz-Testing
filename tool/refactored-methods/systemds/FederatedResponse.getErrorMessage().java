public String getErrorMessage() {
    if (_data == null || _data.length == 0)
        return "No readable error message";
    if (_data[0] instanceof Throwable)
        return ExceptionUtils.getStackTrace((Throwable) _data[0]);
    else if (_data[0] instanceof String)
        return (String) _data[0];
    else
        return "No readable error message";
}
// ---- helper method(s) introduced by the refactoring ----
private long calculateDataSize(Object[] data) {
    long size = 0;
    for (Object obj : data) {
        if (obj instanceof CacheBlock)
            size += ((CacheBlock<?>) obj).getExactSerializedSize();
    }
    return size;
}

