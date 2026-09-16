@Override
public SampleResult sample(Entry e) {
    SampleResult res = new SampleResult();
    res.setSampleLabel(getName());
    res.setSamplerData(toString());
    res.setDataType(SampleResult.TEXT);
    // $NON-NLS-1$
    res.setContentType("text/plain");
    res.setDataEncoding(ENCODING);
    // Assume we will be successful
    res.setSuccessful(true);
    res.setResponseMessageOK();
    res.setResponseCodeOK();
    res.sampleStart();
    Connection conn = null;
    try {
        String dataSource = getDataSource();
        if (JOrphanUtils.isBlank(dataSource)) {
            throw new IllegalArgumentException("Name for DataSoure must not be empty in " + getName());
        }
        try {
            conn = DataSourceElement.getConnection(dataSource);
        } finally {
            res.connectEnd();
        }
        res.setResponseHeaders(DataSourceElement.getConnectionInfo(dataSource));
        res.setResponseData(execute(conn, res));
    } catch (SQLException ex) {
        final String errCode = Integer.toString(ex.getErrorCode());
        res.setResponseMessage(ex.toString());
        res.setResponseCode(ex.getSQLState() + " " + errCode);
        res.setResponseData(ex.getMessage(), res.getDataEncodingWithDefault());
        res.setSuccessful(false);
    } catch (Exception ex) {
        res.setResponseMessage(ex.toString());
        res.setResponseCode("000");
        res.setResponseData(ObjectUtils.defaultIfNull(ex.getMessage(), "NO MESSAGE"), res.getDataEncodingWithDefault());
        res.setSuccessful(false);
    } finally {
        close(conn);
    }
    // TODO: process warnings? Set Code and Message to success?
    res.sampleEnd();
    return res;
}