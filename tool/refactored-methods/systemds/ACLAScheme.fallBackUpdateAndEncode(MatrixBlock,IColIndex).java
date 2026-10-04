private final Pair<ICLAScheme, AColGroup> fallBackUpdateAndEncode(MatrixBlock data, IColIndex columns) {
    ICLAScheme s = update(data, columns);
    AColGroup g = s.encode(data, columns);
    return new Pair<>(s, g);
}