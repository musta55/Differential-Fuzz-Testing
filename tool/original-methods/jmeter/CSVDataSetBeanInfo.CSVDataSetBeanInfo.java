public CSVDataSetBeanInfo() {
    super(CSVDataSet.class);
    //$NON-NLS-1$
    createPropertyGroup(//$NON-NLS-1$
    "csv_data", new String[] { FILENAME, FILE_ENCODING, VARIABLE_NAMES, IGNORE_FIRST_LINE, DELIMITER, QUOTED_DATA, RECYCLE, STOPTHREAD, SHAREMODE });
    PropertyDescriptor p = property(FILENAME);
    p.setValue(NOT_UNDEFINED, Boolean.TRUE);
    //$NON-NLS-1$
    p.setValue(DEFAULT, "");
    p.setValue(NOT_EXPRESSION, Boolean.TRUE);
    p.setPropertyEditorClass(FileEditor.class);
    p = property(FILE_ENCODING, TypeEditor.ComboStringEditor);
    p.setValue(NOT_UNDEFINED, Boolean.TRUE);
    //$NON-NLS-1$
    p.setValue(DEFAULT, "");
    p.setValue(TAGS, getListFileEncoding());
    p = property(VARIABLE_NAMES);
    p.setValue(NOT_UNDEFINED, Boolean.TRUE);
    //$NON-NLS-1$
    p.setValue(DEFAULT, "");
    p.setValue(NOT_EXPRESSION, Boolean.TRUE);
    p = property(IGNORE_FIRST_LINE);
    p.setValue(NOT_UNDEFINED, Boolean.TRUE);
    p.setValue(DEFAULT, Boolean.FALSE);
    p = property(DELIMITER);
    p.setValue(NOT_UNDEFINED, Boolean.TRUE);
    //$NON-NLS-1$
    p.setValue(DEFAULT, ",");
    p.setValue(NOT_EXPRESSION, Boolean.TRUE);
    p = property(QUOTED_DATA);
    p.setValue(NOT_UNDEFINED, Boolean.TRUE);
    p.setValue(DEFAULT, Boolean.FALSE);
    p = property(RECYCLE);
    p.setValue(NOT_UNDEFINED, Boolean.TRUE);
    p.setValue(DEFAULT, Boolean.TRUE);
    p = property(STOPTHREAD);
    p.setValue(NOT_UNDEFINED, Boolean.TRUE);
    p.setValue(DEFAULT, Boolean.FALSE);
    p = property(SHAREMODE, TypeEditor.ComboStringEditor);
    p.setValue(RESOURCE_BUNDLE, getBeanDescriptor().getValue(RESOURCE_BUNDLE));
    p.setValue(NOT_UNDEFINED, Boolean.TRUE);
    p.setValue(DEFAULT, SHARE_TAGS[SHARE_ALL]);
    p.setValue(NOT_OTHER, Boolean.FALSE);
    p.setValue(NOT_EXPRESSION, Boolean.FALSE);
    p.setValue(TAGS, SHARE_TAGS);
}