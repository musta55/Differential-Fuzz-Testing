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
    BSFEngine bsfEngine = null;
    // There's little point saving the manager between invocations
    // as we need to reset most of the beans anyway
    BSFManager mgr = new BSFManager();
    // TODO: find out how to retrieve these from the script
    // At present the script has to use SampleResult methods to set them.
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
        // N.B. some engines (e.g. Javascript) cannot handle certain declareBean() calls
        // after the engine has been initialised, so create the engine last
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
    return res;
}