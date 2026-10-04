public static MatrixReader createMatrixReader(ReadProperties props) {
    //check valid read properties
    if (props == null)
        throw new DMLRuntimeException("Failed to create matrix reader with empty properties.");
    MatrixReader reader = null;
    FileFormat fmt = props.fmt;
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
            reader = (par & mcsr) ? new ReaderTextCSVParallel(props.formatProperties != null ? (FileFormatPropertiesCSV) props.formatProperties : new FileFormatPropertiesCSV()) : new ReaderTextCSV(props.formatProperties != null ? (FileFormatPropertiesCSV) props.formatProperties : new FileFormatPropertiesCSV());
            break;
        case LIBSVM:
            FileFormatPropertiesLIBSVM fileFormatPropertiesLIBSVM = props.formatProperties != null ? (FileFormatPropertiesLIBSVM) props.formatProperties : new FileFormatPropertiesLIBSVM();
            reader = (par & mcsr) ? new ReaderTextLIBSVMParallel(fileFormatPropertiesLIBSVM) : new ReaderTextLIBSVM(fileFormatPropertiesLIBSVM);
            break;
        case BINARY:
            reader = (par & mcsr) ? new ReaderBinaryBlockParallel(props.localFS) : new ReaderBinaryBlock(props.localFS);
            break;
        case HDF5:
            FileFormatPropertiesHDF5 fileFormatPropertiesHDF5 = props.formatProperties != null ? (FileFormatPropertiesHDF5) props.formatProperties : new FileFormatPropertiesHDF5();
            reader = (par & mcsr) ? new ReaderHDF5Parallel(fileFormatPropertiesHDF5) : new ReaderHDF5(fileFormatPropertiesHDF5);
            break;
        case COG:
            FileFormatPropertiesCOG fileFormatPropertiesCOG = props.formatProperties != null ? (FileFormatPropertiesCOG) props.formatProperties : new FileFormatPropertiesCOG();
            reader = (par & mcsr) ? new ReaderCOGParallel(fileFormatPropertiesCOG) : new ReaderCOG(fileFormatPropertiesCOG);
            break;
        case COMPRESSED:
            reader = new ReaderCompressed();
            break;
        case DELTA:
            reader = par ? new ReaderDeltaParallel() : new ReaderDelta();
            break;
        default:
            throw new DMLRuntimeException("Failed to create matrix reader for unknown format: " + fmt.toString());
    }
    return reader;
}