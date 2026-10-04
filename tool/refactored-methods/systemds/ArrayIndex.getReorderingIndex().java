@Override
public int[] getReorderingIndex() {
    return IntStream.range(0, cols.length).boxed().sorted((i, j) -> Integer.compare(cols[i], cols[j])).mapToInt(Integer::intValue).toArray();
}
// ---- helper method(s) introduced by the refactoring ----
private int findStartIndex(int lowerBound) {
    int index = Arrays.binarySearch(cols, lowerBound);
    return index < 0 ? Math.abs(index + 1) : index;
}

private int findEndIndex(int upperBound) {
    int index = Arrays.binarySearch(cols, upperBound);
    return index < 0 ? Math.abs(index + 1) : index;
}

private int[] createSlicedArray(int startIndex, int endIndex, int lowerBound) {
    if (lowerBound == 0)
        return Arrays.copyOfRange(cols, startIndex, endIndex);
    int[] result = new int[endIndex - startIndex];
    for (int i = startIndex, j = 0; i < endIndex; i++, j++) result[j] = cols[i] - lowerBound;
    return result;
}

private boolean matchesRangeIndex(IColIndex other) {
    return other.get(0) == cols[0] && other.get(size() - 1) == cols[size() - 1];
}

private boolean matchesGenericIndex(IColIndex other) {
    for (int i = 0; i < size(); i++) if (other.get(i) != cols[i])
        return false;
    return true;
}

private boolean canCombineIntoRange(IColIndex other) {
    int totalSize = size() + other.size();
    int minCombined = Math.min(get(0), other.get(0));
    int maxCombined = Math.max(get(size() - 1), other.get(other.size() - 1));
    return totalSize == maxCombined - minCombined + 1;
}

private int[] mergeArrays(IColIndex other) {
    int[] result = new int[size() + other.size()];
    int leftIndex = 0, rightIndex = 0, resultIndex = 0;
    while (leftIndex < size() && rightIndex < other.size()) {
        int leftValue = get(leftIndex);
        int rightValue = other.get(rightIndex);
        if (leftValue < rightValue) {
            result[resultIndex++] = leftValue;
            leftIndex++;
        } else {
            result[resultIndex++] = rightValue;
            rightIndex++;
        }
    }
    while (leftIndex < size()) result[resultIndex++] = get(leftIndex++);
    while (rightIndex < other.size()) result[resultIndex++] = other.get(rightIndex++);
    return result;
}

