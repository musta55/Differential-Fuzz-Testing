@Override
public int[] getReorderingIndex() {
    return id2 < id1 ? new int[] { 1, 0 } : new int[] { 0, 1 };
}