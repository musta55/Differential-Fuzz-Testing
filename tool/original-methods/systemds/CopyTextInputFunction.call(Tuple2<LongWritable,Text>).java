@Override
public Tuple2<LongWritable, Text> call(Tuple2<LongWritable, Text> arg0) throws Exception {
    LongWritable lw = new LongWritable(arg0._1().get());
    Text txt = new Text(arg0._2());
    return new Tuple2<>(lw, txt);
}