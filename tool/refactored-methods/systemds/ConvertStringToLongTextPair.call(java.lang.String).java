@Override
public Tuple2<LongWritable, Text> call(String input) {
    return new Tuple2<>(new LongWritable(1), new Text(input));
}