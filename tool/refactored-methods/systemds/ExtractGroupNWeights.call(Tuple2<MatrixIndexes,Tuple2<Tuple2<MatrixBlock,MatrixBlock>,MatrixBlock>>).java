@Override
public Iterator<Tuple2<MatrixIndexes, WeightedCell>> call(Tuple2<MatrixIndexes, Tuple2<Tuple2<MatrixBlock, MatrixBlock>, MatrixBlock>> input) throws Exception {
    MatrixBlock groupBlock = input._2._1._1;
    MatrixBlock targetBlock = input._2._1._2;
    MatrixBlock weightBlock = input._2._2;
    validateBlockDimensions(groupBlock, targetBlock, weightBlock);
    //output weighted cells
    ArrayList<Tuple2<MatrixIndexes, WeightedCell>> groupValuePairs = new ArrayList<>();
    for (int rowIndex = 0; rowIndex < groupBlock.getNumRows(); rowIndex++) {
        WeightedCell weightedCell = new WeightedCell();
        weightedCell.setValue(targetBlock.get(rowIndex, 0));
        weightedCell.setWeight(weightBlock.get(rowIndex, 0));
        long groupValue = UtilFunctions.toLong(groupBlock.get(rowIndex, 0));
        validateGroupValue(groupValue);
        MatrixIndexes indexes = new MatrixIndexes(groupValue, 1);
        groupValuePairs.add(new Tuple2<>(indexes, weightedCell));
    }
    return groupValuePairs.iterator();
}
// ---- helper method(s) introduced by the refactoring ----
private void validateBlockDimensions(MatrixBlock groupBlock, MatrixBlock targetBlock, MatrixBlock weightBlock) throws Exception {
    if (groupBlock.getNumRows() != targetBlock.getNumRows() || groupBlock.getNumRows() != weightBlock.getNumRows()) {
        throw new Exception("The blocksize for group/target/weight blocks are mismatched: " + groupBlock.getNumRows() + ", " + targetBlock.getNumRows() + ", " + weightBlock.getNumRows());
    }
}

private void validateGroupValue(long groupValue) throws Exception {
    if (groupValue < 1) {
        throw new Exception("Expected group values to be greater than equal to 1 but found " + groupValue);
    }
}

