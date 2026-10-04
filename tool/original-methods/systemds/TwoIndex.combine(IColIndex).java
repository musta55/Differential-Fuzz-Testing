@Override
public IColIndex combine(IColIndex other) {
    if (other instanceof SingleIndex) {
        int otherV = other.get(0);
        if (otherV < id1)
            return ColIndexFactory.create(new int[] { otherV, id1, id2 });
        else if (otherV < id2)
            return ColIndexFactory.create(new int[] { id1, otherV, id2 });
        else
            return ColIndexFactory.create(new int[] { id1, id2, otherV });
    } else if (other instanceof TwoIndex) {
        int[] vals = new int[] { other.get(0), other.get(1), id1, id2 };
        Arrays.sort(vals);
        return ColIndexFactory.create(vals);
    } else
        return other.combine(this);
}