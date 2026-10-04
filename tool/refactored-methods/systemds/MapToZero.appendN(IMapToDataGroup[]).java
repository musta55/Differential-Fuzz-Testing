@Override
public AMapToData appendN(IMapToDataGroup[] d) {
    int newSize = calculateNewSize(d);
    if (newSize == -1)
        throw new RuntimeException("Not supported combining different types of map");
    return new MapToZero(newSize);
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

