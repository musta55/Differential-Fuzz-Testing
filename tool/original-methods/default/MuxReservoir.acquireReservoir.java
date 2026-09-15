public SweepableReservoir acquireReservoir(String id, int capacity) {
    SubReservoir r = reservoirMap.get(id);
    if (r == null) {
        reservoirMap.put(id, r = new SubReservoir(capacity));
        SubReservoir[] newReservoirs = new SubReservoir[reservoirs.length + 1];
        newReservoirs[reservoirs.length] = r;
        for (int i = reservoirs.length; i-- > 0; ) {
            newReservoirs[i] = reservoirs[i];
        }
        reservoirs = newReservoirs;
    }
    return r;
}