@Override
public Tuple2<Long, FrameBlock> call(Tuple2<LongWritable, FrameBlock> input) throws Exception {
    Long key = extractKey(input);
    FrameBlock value = createValue(input);
    return new Tuple2<>(key, value);
}
// ---- helper method(s) introduced by the refactoring ----
private Long extractKey(Tuple2<LongWritable, FrameBlock> input) {
    return input._1().get();
}

private FrameBlock createValue(Tuple2<LongWritable, FrameBlock> input) {
    return _deepCopy ? new FrameBlock(input._2()) : input._2();
}

