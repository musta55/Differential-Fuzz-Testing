@Override
public // Entry tends to be ignored ...
SampleResult // Entry tends to be ignored ...
sample(// Entry tends to be ignored ...
Entry e) {
    final String label = getName();
    final String request = getScript();
    final String fileName = getFilename();
    log.debug("{} {}", label, fileName);
    SampleResult res = new SampleResult();
    res.setSampleLabel(label);
    executeScript(res, request, fileName);
    return res;
}
// ---- helper method(s) introduced by the refactoring ----
private void executeScript(SampleResult res, String request, String fileName) {
    BSFEngine bsfEngine = null;
    BSFManager mgr = new BSFManager();
    // $NON-NLS-1$
    res.setResponseCode("200");
    // $NON-NLS-1$
    res.setResponseMessage("OK");
    res.setSuccessful(true);
    // Default (can be overridden by the script)
    res.setDataType(SampleResult.TEXT);
    res.sampleStart();
    try {
        initManager(mgr);
        // $NON-NLS-1$
        mgr.declareBean("SampleResult", res, res.getClass());
        bsfEngine = mgr.loadScriptingEngine(getScriptLanguage());
        Object bsfOut = null;
        if (fileName.length() > 0) {
            res.setSamplerData("File: " + fileName);
            try (FileInputStream fis = new FileInputStream(fileName);
                BufferedInputStream is = new BufferedInputStream(fis)) {
                bsfOut = bsfEngine.eval(fileName, 0, 0, IOUtils.toString(is, Charset.defaultCharset()));
            }
        } else {
            res.setSamplerData(request);
            bsfOut = bsfEngine.eval("script", 0, 0, request);
        }
        if (bsfOut != null) {
            res.setResponseData(bsfOut.toString(), null);
        }
    } catch (BSFException ex) {
        log.warn("BSF error", ex);
        res.setSuccessful(false);
        // $NON-NLS-1$
        res.setResponseCode("500");
        res.setResponseMessage(ex.toString());
    } catch (Exception ex) {
        // Catch evaluation errors
        log.warn("Problem evaluating the script", ex);
        res.setSuccessful(false);
        // $NON-NLS-1$
        res.setResponseCode("500");
        res.setResponseMessage(ex.toString());
    } finally {
        res.sampleEnd();
        mgr.terminate();
    }
}

