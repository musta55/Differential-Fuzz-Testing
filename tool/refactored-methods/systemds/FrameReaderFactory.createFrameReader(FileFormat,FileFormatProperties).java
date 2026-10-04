public static FrameReader createFrameReader(FileFormat fmt, FileFormatProperties props) {
    boolean textParallel = ConfigurationManager.getCompilerConfigFlag(ConfigType.PARALLEL_CP_READ_TEXTFORMATS);
    boolean binaryParallel = ConfigurationManager.getCompilerConfigFlag(ConfigType.PARALLEL_CP_READ_BINARYFORMATS);
    switch(fmt) {
        case TEXT:
            return getTextFrameReader(textParallel);
        case CSV:
            return getCSVFrameReader(props, textParallel);
        // use same logic as a binary read
        case COMPRESSED:
        case BINARY:
            return getBinaryFrameReader(binaryParallel);
        case PROTO:
            // TODO performance improvement: add parallel reader
            return new FrameReaderProto();
        case DELTA:
            return getDeltaFrameReader(textParallel);
        default:
            throw new DMLRuntimeException("Failed to create frame reader for unknown format: " + fmt.toString());
    }
}
// ---- helper method(s) introduced by the refactoring ----
private static FrameReader getTextFrameReader(boolean textParallel) {
    return textParallel ? new FrameReaderTextCellParallel() : new FrameReaderTextCell();
}

private static FrameReader getCSVFrameReader(FileFormatProperties props, boolean textParallel) {
    if (props != null && !(props instanceof FileFormatPropertiesCSV))
        throw new DMLRuntimeException("Wrong type of file format properties for CSV writer.");
    FileFormatPropertiesCSV fp = (FileFormatPropertiesCSV) props;
    return textParallel ? new FrameReaderTextCSVParallel(fp) : new FrameReaderTextCSV(fp);
}

private static FrameReader getBinaryFrameReader(boolean binaryParallel) {
    return binaryParallel ? new FrameReaderBinaryBlockParallel() : new FrameReaderBinaryBlock();
}

private static FrameReader getDeltaFrameReader(boolean textParallel) {
    return textParallel ? new FrameReaderDeltaParallel() : new FrameReaderDelta();
}

