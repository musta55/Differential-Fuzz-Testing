@Override
public boolean equals(Object o) {
    if (!(o instanceof InterestingPoint)) {
        return false;
    }
    InterestingPoint that = (InterestingPoint) o;
    return _fromHopID == that._fromHopID && _toHopID == that._toHopID;
}