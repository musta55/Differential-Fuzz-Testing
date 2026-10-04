public static MatrixReader createMatrixReader(FileFormat fmt) {
    boolean par = ConfigurationManager.getCompilerConfigFlag(ConfigType.PARALLEL_CP_READ_TEXTFORMATS);
    boolean mcsr = MatrixBlock.DEFAULT_SPARSEBLOCK == SparseBlock.Type.MCSR;
    if (LOG.isDebugEnabled()) {
        LOG.debug("reading parallel: " + par + " mcsr: " + mcsr);
    }
    return createMatrixReaderInternal(fmt, par, mcsr, null);
}
// ---- helper method(s) introduced by the refactoring ----
private static MatrixReader createMatrixReaderInternal(FileFormat fmt, boolean par, boolean mcsr, Object formatProperties) {
    MatrixReader reader;
    switch(fmt) {
        case TEXT:
        case MM:
            reader = (par & mcsr) ? new ReaderTextCellParallel(fmt) : new ReaderTextCell(fmt);
            break;
        case CSV:
            FileFormatPropertiesCSV csvProps = getFormatProperties(formatProperties, FileFormatPropertiesCSV.class);
            reader = (par & mcsr) ? new ReaderTextCSVParallel(csvProps) : new ReaderTextCSV(csvProps);
            break;
        case LIBSVM:
            FileFormatPropertiesLIBSVM libsvmProps = getFormatProperties(formatProperties, FileFormatPropertiesLIBSVM.class);
            reader = (par & mcsr) ? new ReaderTextLIBSVMParallel(libsvmProps) : new ReaderTextLIBSVM(libsvmProps);
            break;
        case BINARY:
            boolean localFS = formatProperties != null ? (Boolean) formatProperties : false;
            reader = (par & mcsr) ? new ReaderBinaryBlockParallel(localFS) : new ReaderBinaryBlock(localFS);
            break;
        case HDF5:
            FileFormatPropertiesHDF5 hdf5Props = getFormatProperties(formatProperties, FileFormatPropertiesHDF5.class);
            reader = (par & mcsr) ? new ReaderHDF5Parallel(hdf5Props) : new ReaderHDF5(hdf5Props);
            break;
        case COG:
            FileFormatPropertiesCOG cogProps = getFormatProperties(formatProperties, FileFormatPropertiesCOG.class);
            reader = (par & mcsr) ? new ReaderCOGParallel(cogProps) : new ReaderCOG(cogProps);
            break;
        case COMPRESSED:
            reader = ReaderCompressed.create();
            break;
        case DELTA:
            reader = par ? new ReaderDeltaParallel() : new ReaderDelta();
            break;
        default:
            throw new DMLRuntimeException("Failed to create matrix reader for unknown format: " + fmt.toString());
    }
    return reader;
}

private static <T> T getFormatProperties(Object formatProperties, Class<T> clazz) {
    return formatProperties != null ? clazz.cast(formatProperties) : createDefaultInstance(clazz);
}

private static <T> T createDefaultInstance(Class<T> clazz) {
    try {
        return clazz.getDeclaredConstructor().newInstance();
    } catch (Exception e) {
        throw new DMLRuntimeException("Failed to create default instance of " + clazz.getName(), e);
    }
}

