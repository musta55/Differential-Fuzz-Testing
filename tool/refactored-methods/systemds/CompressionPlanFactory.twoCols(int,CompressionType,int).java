public static IPlanEncode twoCols(int nCol, CompressionType type, int k) {
    ICLAScheme[] schemes = new ICLAScheme[(nCol / 2) + (nCol % 2)];
    for (int i = 0; i < nCol; i += 2) {
        schemes[i / 2] = i + 1 < nCol ? SchemeFactory.create(new TwoIndex(i, i + 1), type) : SchemeFactory.create(new SingleIndex(i), type);
    }
    return new NaivePlanEncode(schemes, k, false);
}