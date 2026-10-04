@Override
public IColIndex combine(IColIndex other) {
    if (other instanceof SingleIndex) {
        int otherV = other.get(0);
        if (otherV < idx)
            return new TwoIndex(otherV, idx);
        else
            return new TwoIndex(idx, otherV);
    } else
        return other.combine(this);
}