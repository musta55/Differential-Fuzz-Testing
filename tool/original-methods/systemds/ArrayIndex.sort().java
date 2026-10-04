@Override
public IColIndex sort() {
    int[] ret = new int[cols.length];
    System.arraycopy(cols, 0, ret, 0, cols.length);
    Arrays.sort(ret);
    return ColIndexFactory.create(ret);
}