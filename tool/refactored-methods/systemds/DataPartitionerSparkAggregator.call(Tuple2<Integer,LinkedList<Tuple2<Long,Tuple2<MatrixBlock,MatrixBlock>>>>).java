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
    aggregateRowWise(input._2, fmb, lmb);
    return new Tuple2<>(input._1, new Tuple2<>(fmb, lmb));
}
// ---- helper method(s) introduced by the refactoring ----
private void aggregateRowWise(LinkedList<Tuple2<Long, Tuple2<MatrixBlock, MatrixBlock>>> inputList, MatrixBlock fmb, MatrixBlock lmb) {
    for (int i = 0; i < inputList.size(); i++) {
        MatrixBlock tmpFMB = inputList.get(i)._2._1;
        MatrixBlock tmpLMB = inputList.get(i)._2._2;
        // Row-wise aggregation
        fmb = fmb.leftIndexingOperations(tmpFMB, i, i, 0, (int) _fcol - 1, fmb, MatrixObject.UpdateType.INPLACE_PINNED);
        lmb = lmb.leftIndexingOperations(tmpLMB, i, i, 0, (int) _lcol - 1, lmb, MatrixObject.UpdateType.INPLACE_PINNED);
    }
}

