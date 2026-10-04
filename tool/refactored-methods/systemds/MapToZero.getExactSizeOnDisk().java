@Override
public long getExactSizeOnDisk() {
    return 1 + Integer.BYTES;
}
// ---- helper method(s) introduced by the refactoring ----
private int calculateNewSize(IMapToDataGroup[] d) {
    // pointer
    int p = 0;
    for (IMapToDataGroup gd : d) {
        AMapToData m = gd.getMapToData();
        if (!(m instanceof MapToZero))
            return -1;
        p += m.size();
    }
    return p;
}

