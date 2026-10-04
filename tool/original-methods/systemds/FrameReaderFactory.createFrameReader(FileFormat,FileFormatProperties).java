public static FrameReader createFrameReader(FileFormat fmt, FileFormatProperties props) {
    boolean textParallel = ConfigurationManager.getCompilerConfigFlag(ConfigType.PARALLEL_CP_READ_TEXTFORMATS);
    boolean binaryParallel = ConfigurationManager.getCompilerConfigFlag(ConfigType.PARALLEL_CP_READ_BINARYFORMATS);
    switch(fmt) {
        case TEXT:
            return textParallel ? new FrameReaderTextCellParallel() : new FrameReaderTextCell();
        case CSV:
            if (props != null && !(props instanceof FileFormatPropertiesCSV))
                throw new DMLRuntimeException("Wrong type of file format properties for CSV writer.");
            FileFormatPropertiesCSV fp = (FileFormatPropertiesCSV) props;
            return textParallel ? new FrameReaderTextCSVParallel(fp) : new FrameReaderTextCSV(fp);
        // use same logic as a binary read
        case COMPRESSED:
        case BINARY:
            return binaryParallel ? new FrameReaderBinaryBlockParallel() : new FrameReaderBinaryBlock();
        case PROTO:
            // TODO performance improvement: add parallel reader
            return new FrameReaderProto();
        case DELTA:
            return textParallel ? new FrameReaderDeltaParallel() : new FrameReaderDelta();
        default:
            throw new DMLRuntimeException("Failed to create frame reader for unknown format: " + fmt.toString());
    }
}