@Override
public Iterator<String> call(Tuple2<MatrixIndexes, MatrixBlock> kv) {
    BinaryBlockToTextCellConverter converter = createAndConfigureConverter(blen, kv._1, kv._2);
    return new IJVLineIterator(converter);
}
// ---- helper method(s) introduced by the refactoring ----
private BinaryBlockToTextCellConverter createAndConfigureConverter(int blen, MatrixIndexes indexes, MatrixBlock block) {
    BinaryBlockToTextCellConverter converter = new BinaryBlockToTextCellConverter();
    converter.setBlockSize(blen, blen);
    converter.convert(indexes, block);
    return converter;
}

