public SweepableReservoir releaseReservoir(String id) {
    SubReservoir r = reservoirMap.remove(id);
    if (r != null) {
        SubReservoir[] newReservoirs = new SubReservoir[reservoirs.length - 1];
        int j = 0;
        for (int i = 0; i < reservoirs.length; i++) {
            if (reservoirs[i] != r) {
                newReservoirs[j++] = reservoirs[i];
            }
        }
        reservoirs = newReservoirs;
    }
    return r;
}