@Override
public int[] getReorderingIndex() {
    int[] sortedIndices = //
    IntStream.range(0, cols.length).boxed().sorted(//
    (i, j) -> Integer.valueOf(cols[i]).compareTo(cols[j])).mapToInt(ele -> ele).toArray();
    return sortedIndices;
}