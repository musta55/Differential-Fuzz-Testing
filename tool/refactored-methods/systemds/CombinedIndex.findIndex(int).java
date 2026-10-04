@Override
public int findIndex(int i) {
    int leftIndex = l.findIndex(i);
    if (leftIndex >= 0)
        return leftIndex;
    int rightIndex = r.findIndex(i);
    return rightIndex < 0 ? rightIndex + leftIndex + 1 : rightIndex + l.size();
}
// ---- helper method(s) introduced by the refactoring ----
private boolean equalsCombinedIndex(CombinedIndex other) {
    return other.l.equals(l) && other.r.equals(r);
}

private boolean equalsOtherIndex(IColIndex other) {
    IIterate thisIterator = iterator();
    IIterate otherIterator = other.iterator();
    while (thisIterator.hasNext()) {
        if (thisIterator.next() != otherIterator.next())
            return false;
    }
    return true;
}

private boolean canCombineIntoRangeIndex(IColIndex other) {
    int otherSize = other.size();
    int thisSize = size();
    int maxCombined = Math.max(get(thisSize - 1), other.get(otherSize - 1));
    int minCombined = Math.min(get(0), other.get(0));
    return otherSize + thisSize == maxCombined - minCombined + 1;
}

private IColIndex createRangeIndex(IColIndex other) {
    int minCombined = Math.min(get(0), other.get(0));
    int maxCombined = Math.max(get(size() - 1), other.get(other.size() - 1));
    return new RangeIndex(minCombined, maxCombined + 1);
}

private IColIndex createCombinedIndex(IColIndex other) {
    int[] combinedValues = new int[size() + other.size()];
    IIterate thisIterator = iterator();
    IIterate otherIterator = other.iterator();
    int index = 0;
    while (thisIterator.hasNext() && otherIterator.hasNext()) {
        int thisValue = thisIterator.v();
        int otherValue = otherIterator.v();
        if (thisValue < otherValue) {
            combinedValues[index++] = thisValue;
            thisIterator.next();
        } else {
            combinedValues[index++] = otherValue;
            otherIterator.next();
        }
    }
    while (thisIterator.hasNext()) combinedValues[index++] = thisIterator.next();
    while (otherIterator.hasNext()) combinedValues[index++] = otherIterator.next();
    return ColIndexFactory.create(combinedValues);
}

