@Override
public IColIndex shift(int i) {
    int[] ret = new int[cols.length];
    for (int j = 0; j < cols.length; j++) ret[j] = cols[j] + i;
    return new ArrayIndex(ret);
}