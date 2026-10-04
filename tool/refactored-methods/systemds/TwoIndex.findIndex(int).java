@Override
public int findIndex(int i) {
    if (i < id1)
        return -1;
    if (i == id1)
        return 0;
    if (i < id2)
        return -2;
    if (i == id2)
        return 1;
    return -3;
}