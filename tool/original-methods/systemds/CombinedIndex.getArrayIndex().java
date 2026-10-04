private IColIndex getArrayIndex() {
    int s = size();
    int[] vals = new int[s];
    IIterate a = iterator();
    for (int i = 0; i < s; i++) {
        vals[i] = a.next();
    }
    return ColIndexFactory.create(vals);
}