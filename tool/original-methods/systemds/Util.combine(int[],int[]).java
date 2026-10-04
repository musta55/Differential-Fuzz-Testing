public static int[] combine(int[] lhs, int[] rhs) {
    int[] joined = new int[lhs.length + rhs.length];
    int lp = 0;
    int rp = 0;
    int i = 0;
    for (; i < joined.length && lp < lhs.length && rp < rhs.length; i++) {
        if (lhs[lp] < rhs[rp])
            joined[i] = lhs[lp++];
        else
            joined[i] = rhs[rp++];
    }
    while (lp < lhs.length) joined[i++] = lhs[lp++];
    while (rp < rhs.length) joined[i++] = rhs[rp++];
    return joined;
}