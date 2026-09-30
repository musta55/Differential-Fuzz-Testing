public SweepableReservoir releaseReservoir(String id) {
    SubReservoir r = reservoirMap.remove(id);
    if (r != null) {
        resizeReservoirs(reservoirs.length - 1, r);
    }
    return r;
}
// ---- helper method(s) introduced by the refactoring ----
private void resizeReservoirs(int newSize, SubReservoir exclude) {
    SubReservoir[] newReservoirs = new SubReservoir[newSize];
    int j = 0;
    for (SubReservoir reservoir : reservoirs) {
        if (reservoir != exclude) {
            newReservoirs[j++] = reservoir;
        }
    }
    if (exclude != null) {
        newReservoirs[newSize - 1] = exclude;
    }
    reservoirs = newReservoirs;
}

