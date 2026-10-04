@Override
protected CompressedSizeInfoColGroup combine(IColIndex combinedColumns, CompressedSizeInfoColGroup g1, CompressedSizeInfoColGroup g2, int maxDistinct) {
    IEncode map = g1.getMap().combine(g2.getMap());
    return getFacts(map, combinedColumns);
}