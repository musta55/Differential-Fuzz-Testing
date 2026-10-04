public static FrameWriter createFrameWriter(FileFormat fmt, FileFormatProperties props) {
    boolean textParallel = ConfigurationManager.getCompilerConfigFlag(ConfigType.PARALLEL_CP_WRITE_TEXTFORMATS);
    boolean binaryParallel = ConfigurationManager.getCompilerConfigFlag(ConfigType.PARALLEL_CP_WRITE_BINARYFORMATS);
    switch(fmt) {
        case TEXT:
            return createTextFrameWriter(textParallel);
        case CSV:
            return createCsvFrameWriter(props, textParallel);
        case COMPRESSED:
            return new FrameWriterCompressed(binaryParallel);
        case BINARY:
            return createBinaryFrameWriter(binaryParallel);
        case PROTO:
            return new FrameWriterProto();
        case DELTA:
            return new FrameWriterDelta();
        default:
            throw new DMLRuntimeException("Failed to create frame writer for unknown format: " + fmt.toString());
    }
}
// ---- helper method(s) introduced by the refactoring ----
private static FrameWriter createTextFrameWriter(boolean parallel) {
    return parallel ? new FrameWriterTextCellParallel() : new FrameWriterTextCell();
}

private static FrameWriter createCsvFrameWriter(FileFormatProperties props, boolean parallel) {
    if (props != null && !(props instanceof FileFormatPropertiesCSV))
        throw new DMLRuntimeException("Wrong type of file format properties for CSV writer.");
    FileFormatPropertiesCSV fp = (FileFormatPropertiesCSV) props;
    return parallel ? new FrameWriterTextCSVParallel(fp) : new FrameWriterTextCSV(fp);
}

private static FrameWriter createBinaryFrameWriter(boolean parallel) {
    return parallel ? new FrameWriterBinaryBlockParallel() : new FrameWriterBinaryBlock();
}

