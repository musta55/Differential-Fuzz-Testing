/**
 * Row-wise combine the matrix
 * @param input workerID {@literal =>} ordered list [(rowBlockID, (features, labels))]
 * @return workerID {@literal =>} [(features, labels)]
 * @throws Exception Some exception
 */
@Override
public Tuple2<Integer, Tuple2<MatrixBlock, MatrixBlock>> call(Tuple2<Integer, LinkedList<Tuple2<Long, Tuple2<MatrixBlock, MatrixBlock>>>> input) throws Exception {
    MatrixBlock fmb = new MatrixBlock(input._2.size(), (int) _fcol, false);
    MatrixBlock lmb = new MatrixBlock(input._2.size(), (int) _lcol, false);
    for (int i = 0; i < input._2.size(); i++) {
        MatrixBlock tmpFMB = input._2.get(i)._2._1;
        MatrixBlock tmpLMB = input._2.get(i)._2._2;
        // Row-wise aggregation
        fmb = fmb.leftIndexingOperations(tmpFMB, i, i, 0, (int) _fcol - 1, fmb, MatrixObject.UpdateType.INPLACE_PINNED);
        lmb = lmb.leftIndexingOperations(tmpLMB, i, i, 0, (int) _lcol - 1, lmb, MatrixObject.UpdateType.INPLACE_PINNED);
    }
    return new Tuple2<>(input._1, new Tuple2<>(fmb, lmb));
}