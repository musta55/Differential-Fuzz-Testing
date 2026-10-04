public static IPlanEncode nCols(int nCol, int n, CompressionType type, int k) {
    ICLAScheme[] schemes = new ICLAScheme[(nCol / n) + ((nCol % n != 0) ? 1 : 0)];
    for (int i = 0; i < nCol; i += n) {
        schemes[i / n] = i + n < nCol ? SchemeFactory.create(ColIndexFactory.create(i, i + n), type) : SchemeFactory.create(ColIndexFactory.create(i, nCol), type);
    }
    return new NaivePlanEncode(schemes, k, false);
}