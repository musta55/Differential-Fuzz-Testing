private final Pair<ICLAScheme, AColGroup> fallBackUpdateAndEncodeT(MatrixBlock data, IColIndex columns) {
    ICLAScheme s = updateT(data, columns);
    AColGroup g = s.encodeT(data, columns);
    return new Pair<>(s, g);
}