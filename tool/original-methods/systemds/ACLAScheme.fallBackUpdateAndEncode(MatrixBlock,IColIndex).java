private final Pair<ICLAScheme, AColGroup> fallBackUpdateAndEncode(MatrixBlock data, IColIndex columns) {
    final ICLAScheme s = update(data, columns);
    final AColGroup g = s.encode(data, columns);
    return new Pair<>(s, g);
}