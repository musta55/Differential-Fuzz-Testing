@Override
public boolean computeDimension(int row, int col, CellIndex retDim) {
    if (//diagV2M
    col == 1)
        retDim.set(row, row);
    else
        //diagM2V
        retDim.set(row, 1);
    return false;
}