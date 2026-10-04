public double[] getDictionary(int size) {
    double[] ret = new double[size];
    for (int i = 0; i < data.length; i++) {
        ACount<Double> e = data[i];
        while (e != null) {
            if (e.id >= 0)
                ret[e.id] = e.key();
            e = e.next();
        }
    }
    return ret;
}