@Override
public Tuple2<LongWritable, Text> call(Tuple2<LongWritable, Text> arg0) throws Exception {
    return new Tuple2<>(new LongWritable(arg0._1().get()), new Text(arg0._2()));
}