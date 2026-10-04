public static boolean isMatPoint(InterestingPoint[] list, long from, long to) {
    for (int i = 0; i < list.length; i++) {
        InterestingPoint p = list[i];
        if (p._fromHopID == from && p._toHopID == to)
            return true;
    }
    return false;
}