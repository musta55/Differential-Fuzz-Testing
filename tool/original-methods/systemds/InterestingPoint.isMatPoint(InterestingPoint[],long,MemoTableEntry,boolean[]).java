public static boolean isMatPoint(InterestingPoint[] list, long from, MemoTableEntry me, boolean[] plan) {
    for (int i = 0; i < plan.length; i++) {
        if (!plan[i])
            continue;
        InterestingPoint p = list[i];
        if (p._fromHopID != from)
            continue;
        for (int j = 0; j < 3; j++) if (p._toHopID == me.input(j))
            return true;
    }
    return false;
}