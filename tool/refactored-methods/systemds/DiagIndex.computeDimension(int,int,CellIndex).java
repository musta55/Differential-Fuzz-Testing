@Override
public boolean computeDimension(int numRows, int numCols, CellIndex retDim) {
    if (//diagV2M
    numCols == 1)
        retDim.set(numRows, numRows);
    else
        //diagM2V
        retDim.set(numRows, 1);
    return false;
}