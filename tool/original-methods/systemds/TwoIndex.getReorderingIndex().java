@Override
public int[] getReorderingIndex() {
    if (id2 < id1)
        return new int[] { 1, 0 };
    else
        return new int[] { 0, 1 };
}