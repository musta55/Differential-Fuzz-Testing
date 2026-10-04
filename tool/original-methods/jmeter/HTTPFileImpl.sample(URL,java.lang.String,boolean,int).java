@Override
protected HTTPSampleResult sample(URL url, String method, boolean areFollowingRedirect, int frameDepth) {
    HTTPSampleResult res = new HTTPSampleResult();
    // Dummy
    res.setHTTPMethod(HTTPConstants.GET);
    res.setURL(url);
    res.setSampleLabel(url.toString());
    InputStream is = null;
    res.sampleStart();
    int bufferSize = 4096;
    try (org.apache.commons.io.output.ByteArrayOutputStream bos = new org.apache.commons.io.output.ByteArrayOutputStream(bufferSize)) {
        byte[] responseData;
        URLConnection conn = url.openConnection();
        is = conn.getInputStream();
        byte[] readBuffer = new byte[bufferSize];
        int bytesReadInBuffer = 0;
        long totalBytes = 0;
        boolean storeInBOS = true;
        while ((bytesReadInBuffer = is.read(readBuffer)) > -1) {
            if (storeInBOS) {
                if (totalBytes + bytesReadInBuffer <= MAX_BYTES_TO_STORE_PER_REQUEST) {
                    bos.write(readBuffer, 0, bytesReadInBuffer);
                } else {
                    bos.write(readBuffer, 0, (int) (MAX_BYTES_TO_STORE_PER_REQUEST - totalBytes));
                    storeInBOS = false;
                }
            }
            totalBytes += bytesReadInBuffer;
        }
        responseData = bos.toByteArray();
        res.sampleEnd();
        res.setResponseData(responseData);
        res.setBodySize(totalBytes);
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