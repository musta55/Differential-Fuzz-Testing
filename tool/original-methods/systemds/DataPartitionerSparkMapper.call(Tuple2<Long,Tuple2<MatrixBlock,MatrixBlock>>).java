/**
 * Do data partitioning
 * @param input RowBlockID {@literal =>} (features, labels)
 * @return WorkerID {@literal =>} (rowBlockID, (single row features, single row labels))
 * @throws Exception Some exception
 */
@Override
public Iterator<Tuple2<Integer, Tuple2<Long, Tuple2<MatrixBlock, MatrixBlock>>>> call(Tuple2<Long, Tuple2<MatrixBlock, MatrixBlock>> input) throws Exception {
    List<Tuple2<Integer, Tuple2<Long, Tuple2<MatrixBlock, MatrixBlock>>>> partitions = new LinkedList<>();
    MatrixBlock features = input._2._1;
    MatrixBlock labels = input._2._2;
    DataPartitionSparkScheme.Result result = _dp.doPartitioning(_workersNum, features, labels, input._1);
    for (int i = 0; i < result.pFeatures.size(); i++) {
        Tuple2<Integer, Tuple2<Long, MatrixBlock>> ft = result.pFeatures.get(i);
        Tuple2<Integer, Tuple2<Long, MatrixBlock>> lt = result.pLabels.get(i);
        partitions.add(new Tuple2<>(ft._1, new Tuple2<>(ft._2._1, new Tuple2<>(ft._2._2, lt._2._2))));
    }
    return partitions.iterator();
}