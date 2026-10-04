public FileFormatPropertiesLIBSVM(String delim, String indexDelim, boolean sparse) {
    this(delim, indexDelim);
    this.sparse = sparse;
    if (LOG.isDebugEnabled())
        LOG.debug("FileFormatPropertiesLIBSVM full settings: " + this.toString());
}