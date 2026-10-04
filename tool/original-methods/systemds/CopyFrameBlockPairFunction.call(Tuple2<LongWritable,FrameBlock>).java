@Override
public Tuple2<Long, FrameBlock> call(Tuple2<LongWritable, FrameBlock> arg0) throws Exception {
    if (_deepCopy) {
        FrameBlock block = new FrameBlock(arg0._2());
        return new Tuple2<>(arg0._1().get(), block);
    } else {
        return new Tuple2<>(arg0._1().get(), arg0._2());
    }
}