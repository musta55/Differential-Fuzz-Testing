@Override
public MatrixBlock call(MatrixBlock matrixBlock) {
    aNnz.add(matrixBlock.getNonZeros());
    return matrixBlock;
}