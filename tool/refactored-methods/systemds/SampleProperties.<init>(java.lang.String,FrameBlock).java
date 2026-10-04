public SampleProperties(String sampleRaw, FrameBlock sampleFrame) {
    this(sampleRaw);
    this.sampleFrame = sampleFrame;
    this.dataType = Types.DataType.FRAME;
}