@Override
public Tuple2<LongWritable, Text> call(String arg0) {
    return new Tuple2<>(new LongWritable(1), new Text(arg0));
}