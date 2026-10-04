public IsBlockInRange(long rowLowerBound, long rowUpperBound, long columnLowerBound, long columnUpperBound, DataCharacteristics matrixCharacteristics) {
    this.rowLowerBound = rowLowerBound;
    this.rowUpperBound = rowUpperBound;
    this.columnLowerBound = columnLowerBound;
    this.columnUpperBound = columnUpperBound;
    this.blockSize = matrixCharacteristics.getBlocksize();
}