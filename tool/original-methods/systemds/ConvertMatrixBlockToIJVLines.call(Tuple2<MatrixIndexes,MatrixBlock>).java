@Override
public Iterator<String> call(Tuple2<MatrixIndexes, MatrixBlock> kv) {
    final BinaryBlockToTextCellConverter converter = new BinaryBlockToTextCellConverter();
    converter.setBlockSize(blen, blen);
    converter.convert(kv._1, kv._2);
    Iterable<String> ret = new Iterable<>() {

        @Override
        public Iterator<String> iterator() {
            return new Iterator<>() {

                @Override
                public void remove() {
                }

                @Override
                public String next() {
                    return converter.next().getValue().toString();
                }

                @Override
                public boolean hasNext() {
                    return converter.hasNext();
                }
            };
        }
    };
    return ret.iterator();
}