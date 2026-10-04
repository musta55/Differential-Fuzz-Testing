@Override
public AMapToData appendN(IMapToDataGroup[] d) {
    // pointer
    int p = 0;
    boolean allZ = true;
    for (IMapToDataGroup gd : d) {
        AMapToData m = gd.getMapToData();
        p += m.size();
        if (!(m instanceof MapToZero))
            allZ = false;
    }
    if (!allZ)
        throw new RuntimeException("Not supported combining different types of map");
    return new MapToZero(p);
}