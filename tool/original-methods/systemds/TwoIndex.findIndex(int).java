@Override
public int findIndex(int i) {
    if (i < id1)
        return -1;
    else if (i == id1)
        return 0;
    else if (i < id2)
        return -2;
    else if (i == id2)
        return 1;
    else
        return -3;
}