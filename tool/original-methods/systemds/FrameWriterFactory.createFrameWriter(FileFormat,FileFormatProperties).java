public static FrameWriter createFrameWriter(FileFormat fmt, FileFormatProperties props) {
    boolean textParallel = ConfigurationManager.getCompilerConfigFlag(ConfigType.PARALLEL_CP_WRITE_TEXTFORMATS);
    boolean binaryParallel = ConfigurationManager.getCompilerConfigFlag(ConfigType.PARALLEL_CP_WRITE_BINARYFORMATS);
    switch(fmt) {
        case TEXT:
            return textParallel ? new FrameWriterTextCellParallel() : new FrameWriterTextCell();
        case CSV:
            if (props != null && !(props instanceof FileFormatPropertiesCSV))
                throw new DMLRuntimeException("Wrong type of file format properties for CSV writer.");
            FileFormatPropertiesCSV fp = (FileFormatPropertiesCSV) props;
            return textParallel ? new FrameWriterTextCSVParallel(fp) : new FrameWriterTextCSV(fp);
        case COMPRESSED:
            return new FrameWriterCompressed(binaryParallel);
        case BINARY:
            return binaryParallel ? new FrameWriterBinaryBlockParallel() : new FrameWriterBinaryBlock();
        case PROTO:
            return new FrameWriterProto();
        case DELTA:
            return new FrameWriterDelta();
        default:
            throw new DMLRuntimeException("Failed to create frame writer for unknown format: " + fmt.toString());
    }
}