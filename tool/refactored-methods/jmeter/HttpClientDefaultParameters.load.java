private static void load(String file, GenericHttpParams params) {
    log.info("Trying httpclient parameters from " + file);
    File f = getFile(file);
    if (!f.exists() || !f.canRead()) {
        log.error("Cannot read parameters file for HttpClient: " + file);
        return;
    }
    log.info("Reading httpclient parameters from " + f.getAbsolutePath());
    Properties props = new Properties();
    try (InputStream is = new FileInputStream(f)) {
        props.load(is);
        processProperties(props, params);
    } catch (IOException e) {
        log.error("Problem loading properties " + e.toString());
    }
}
// ---- helper method(s) introduced by the refactoring ----
private static void validateAndSetVersion(org.apache.http.params.HttpParams params, String name, String value) throws Exception {
    String[] parts = value.split("\\.");
    if (parts.length != 2) {
        throw new IllegalArgumentException("Version must have form m.n");
    }
    params.setParameter(name, new org.apache.http.HttpVersion(Integer.parseInt(parts[0]), Integer.parseInt(parts[1])));
}

private static File getFile(String file) {
    File f = new File(file);
    if (!f.exists() || !f.canRead()) {
        f = new File(NewDriver.getJMeterDir() + File.separator + "bin" + File.separator + file);
        log.info(file + " httpclient parameters does not exist, trying " + f.getAbsolutePath());
    }
    return f;
}

private static void processProperties(Properties props, GenericHttpParams params) {
    for (Map.Entry<Object, Object> me : props.entrySet()) {
        String key = (String) me.getKey();
        String value = (String) me.getValue();
        int typeSep = key.indexOf('$');
        try {
            if (typeSep > 0) {
                String type = key.substring(typeSep + 1);
                String name = key.substring(0, typeSep);
                log.info("Defining " + name + " as " + value + " (" + type + ")");
                setParameterByType(params, name, value, type);
            } else {
                log.info("Defining " + key + " as " + value);
                params.setParameter(key, value);
            }
        } catch (Exception e) {
            log.error("Error in property: " + key + "=" + value + " " + e.toString());
        }
    }
}

private static void setParameterByType(GenericHttpParams params, String name, String value, String type) throws Exception {
    switch(type) {
        case "Integer":
            params.setParameter(name, Integer.valueOf(value));
            break;
        case "Long":
            params.setParameter(name, Long.valueOf(value));
            break;
        case "Boolean":
            params.setParameter(name, Boolean.valueOf(value));
            break;
        case "HttpVersion":
            params.setVersion(name, value);
            break;
        default:
            log.warn("Unexpected type: " + type + " for name " + name);
    }
}

