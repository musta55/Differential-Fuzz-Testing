public static int[] combine(int[] lhs, int[] rhs) {
    int[] joined = new int[lhs.length + rhs.length];
    int index = mergeSortedArrays(lhs, rhs, joined);
    appendRemainingElements(lhs, joined, index);
    appendRemainingElements(rhs, joined, index);
    return joined;
}
// ---- helper method(s) introduced by the refactoring ----
private static int mergeSortedArrays(int[] lhs, int[] rhs, int[] joined) {
    int lp = 0, rp = 0, i = 0;
    while (lp < lhs.length && rp < rhs.length) {
        joined[i++] = lhs[lp] < rhs[rp] ? lhs[lp++] : rhs[rp++];
    }
    return i;
}

private static void appendRemainingElements(int[] array, int[] joined, int startIndex) {
    for (int i = startIndex, p = 0; p < array.length; i++, p++) {
        joined[i] = array[p];
    }
}

