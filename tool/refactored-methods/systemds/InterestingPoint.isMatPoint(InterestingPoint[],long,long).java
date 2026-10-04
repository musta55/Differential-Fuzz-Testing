public static boolean isMatPoint(InterestingPoint[] list, long from, long to) {
    for (InterestingPoint p : list) {
        if (p._fromHopID == from && p._toHopID == to) {
            return true;
        }
    }
    return false;
}