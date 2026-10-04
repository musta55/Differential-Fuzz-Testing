@Override
public TensorBlock call(Tuple2<TensorBlock, TensorBlock> arg0) throws Exception {
    return arg0._1().binaryOperations(_bop, arg0._2(), null);
}