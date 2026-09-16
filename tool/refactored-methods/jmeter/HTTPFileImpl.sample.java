@Override
protected HTTPSampleResult sample(URL url, String method, boolean areFollowingRedirect, int frameDepth) {
    HTTPSampleResult res = new HTTPSampleResult();
    // Dummy
    res.setHTTPMethod(HTTPConstants.GET);
    res.setURL(url);
    res.setSampleLabel(url.toString());
    InputStream is = null;
    res.sampleStart();
    try (org.apache.commons.io.output.ByteArrayOutputStream bos = new org.apache.commons.io.output.ByteArrayOutputStream()) {
        byte[] responseData;
        URLConnection conn = url.openConnection();
        is = conn.getInputStream();
        responseData = readInputStream(is, bos);
        res.sampleEnd();
        res.setResponseData(responseData);
        res.setBytes(totalBytesRead);
        res.setResponseCodeOK();
        res.setResponseMessageOK();
        res.setSuccessful(true);
        // $NON-NLS-1$
        StringBuilder ctb = new StringBuilder("text/html");
        // TODO can this be obtained from the file somehow?
        String contentEncoding = getContentEncoding();
        if (contentEncoding.length() > 0) {
            // $NON-NLS-1$
            ctb.append("; charset=");
            ctb.append(contentEncoding);
        }
        String ct = ctb.toString();
        res.setContentType(ct);
        res.setEncodingAndType(ct);
        res = resultProcessing(areFollowingRedirect, frameDepth, res);
        return res;
    } catch (IOException e) {
        return errorResult(e, res);
    } finally {
        IOUtils.closeQuietly(is, null);
    }
}
// ---- helper method(s) introduced by the refactoring ----
private byte[] readInputStream(InputStream is, org.apache.commons.io.output.ByteArrayOutputStream bos) throws IOException {
    int bufferSize = 4096;
    byte[] readBuffer = new byte[bufferSize];
    int bytesReadInBuffer;
    totalBytesRead = 0;
    boolean storeInBOS = true;
    while ((bytesReadInBuffer = is.read(readBuffer)) > -1) {
        if (storeInBOS) {
            if (totalBytesRead + bytesReadInBuffer <= MAX_BYTES_TO_STORE_PER_REQUEST) {
                bos.write(readBuffer, 0, bytesReadInBuffer);
            } else {
                bos.write(readBuffer, 0, (int) (MAX_BYTES_TO_STORE_PER_REQUEST - totalBytesRead));
                storeInBOS = false;
            }
        }
        totalBytesRead += bytesReadInBuffer;
    }
    return bos.toByteArray();
}

