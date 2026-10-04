public static MatrixReader createMatrixReader(FileFormat fmt) {
    MatrixReader reader = null;
    boolean par = ConfigurationManager.getCompilerConfigFlag(ConfigType.PARALLEL_CP_READ_TEXTFORMATS);
    boolean mcsr = MatrixBlock.DEFAULT_SPARSEBLOCK == SparseBlock.Type.MCSR;
    if (LOG.isDebugEnabled()) {
        LOG.debug("reading parallel: " + par + " mcsr: " + mcsr);
    }
    switch(fmt) {
        case TEXT:
        case MM:
            reader = (par & mcsr) ? new ReaderTextCellParallel(fmt) : new ReaderTextCell(fmt);
            break;
        case CSV:
            reader = (par & mcsr) ? new ReaderTextCSVParallel(new FileFormatPropertiesCSV()) : new ReaderTextCSV(new FileFormatPropertiesCSV());
            break;
        case LIBSVM:
            reader = (par & mcsr) ? new ReaderTextLIBSVMParallel(new FileFormatPropertiesLIBSVM()) : new ReaderTextLIBSVM(new FileFormatPropertiesLIBSVM());
            break;
        case BINARY:
            reader = (par & mcsr) ? new ReaderBinaryBlockParallel(false) : new ReaderBinaryBlock(false);
            break;
        case HDF5:
            reader = (par & mcsr) ? new ReaderHDF5Parallel(new FileFormatPropertiesHDF5()) : new ReaderHDF5(new FileFormatPropertiesHDF5());
            break;
        case COG:
            reader = (par & mcsr) ? new ReaderCOGParallel(new FileFormatPropertiesCOG()) : new ReaderCOG(new FileFormatPropertiesCOG());
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