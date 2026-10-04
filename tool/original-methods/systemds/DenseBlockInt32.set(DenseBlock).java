@Override
public DenseBlock set(DenseBlock db) {
    double[] data = db.valuesAt(0);
    //TODO investigate potential deadlocks if already in parallel setting w/ commonPool
    Arrays.parallelSetAll(_data, (i) -> UtilFunctions.toInt(data[i]));
    return this;
}