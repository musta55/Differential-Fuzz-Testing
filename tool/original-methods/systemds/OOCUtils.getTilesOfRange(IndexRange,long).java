public static Collection<MatrixIndexes> getTilesOfRange(IndexRange range, long blen) {
    long rs = (range.rowStart - 1) / blen + 1;
    long re = (range.rowEnd - 1) / blen + 1;
    long cs = (range.colStart - 1) / blen + 1;
    long ce = (range.colEnd - 1) / blen + 1;
    if (rs == re) {
        if (cs == ce) {
            return Collections.singleton(new MatrixIndexes(rs, cs));
        } else {
            List<MatrixIndexes> list = new ArrayList<>((int) (ce - cs + 1));
            for (long i = cs; i <= ce; i++) list.add(new MatrixIndexes(rs, i));
            return list;
        }
    }
    List<MatrixIndexes> list = new ArrayList<>((int) ((re - rs + 1) * (ce - cs + 1)));
    for (long r = rs; r <= re; r++) for (long c = cs; c <= ce; c++) list.add(new MatrixIndexes(r, c));
    return list;
}