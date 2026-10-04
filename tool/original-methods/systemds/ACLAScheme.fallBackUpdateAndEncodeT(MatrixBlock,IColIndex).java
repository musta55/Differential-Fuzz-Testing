private final Pair<ICLAScheme, AColGroup> fallBackUpdateAndEncodeT(MatrixBlock data, IColIndex columns) {
    final ICLAScheme s = updateT(data, columns);
    final AColGroup g = s.encodeT(data, columns);
    return new Pair<>(s, g);
}