public SampleProperties(String sampleRaw, MatrixBlock sampleMatrix) {
    this(sampleRaw);
    this.sampleMatrix = sampleMatrix;
    this.dataType = Types.DataType.MATRIX;
}