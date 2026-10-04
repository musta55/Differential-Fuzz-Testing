@Override
public IColIndex combine(IColIndex other) {
    if (other instanceof SingleIndex) {
        int otherV = other.get(0);
        return otherV < idx ? new TwoIndex(otherV, idx) : new TwoIndex(idx, otherV);
    } else
        return other.combine(this);
}