@Override
public Iterator<Tuple2<MatrixIndexes, WeightedCell>> call(Tuple2<MatrixIndexes, Tuple2<Tuple2<MatrixBlock, MatrixBlock>, MatrixBlock>> arg) throws Exception {
    MatrixBlock group = arg._2._1._1;
    MatrixBlock target = arg._2._1._2;
    MatrixBlock weight = arg._2._2;
    //sanity check matching block dimensions
    if (group.getNumRows() != target.getNumRows() || group.getNumRows() != target.getNumRows()) {
        throw new Exception("The blocksize for group/target/weight blocks are mismatched: " + group.getNumRows() + ", " + target.getNumRows() + ", " + weight.getNumRows());
    }
    //output weighted cells
    ArrayList<Tuple2<MatrixIndexes, WeightedCell>> groupValuePairs = new ArrayList<>();
    for (int i = 0; i < group.getNumRows(); i++) {
        WeightedCell weightedCell = new WeightedCell();
        weightedCell.setValue(target.get(i, 0));
        weightedCell.setWeight(weight.get(i, 0));
        long groupVal = UtilFunctions.toLong(group.get(i, 0));
        if (groupVal < 1) {
            throw new Exception("Expected group values to be greater than equal to 1 but found " + groupVal);
        }
        MatrixIndexes ix = new MatrixIndexes(groupVal, 1);
        groupValuePairs.add(new Tuple2<>(ix, weightedCell));
    }
    return groupValuePairs.iterator();
}