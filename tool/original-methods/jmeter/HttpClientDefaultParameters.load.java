private static void load(String file, GenericHttpParams params) {
    log.info("Trying httpclient parameters from " + file);
    File f = new File(file);
    if (!(f.exists() && f.canRead())) {
        f = new File(NewDriver.getJMeterDir() + File.separator + "bin" + File.separator + // $NON-NLS-1$
        file);
        log.info(file + " httpclient parameters does not exist, trying " + f.getAbsolutePath());
        if (!(f.exists() && f.canRead())) {
            log.error("Cannot read parameters file for HttpClient: " + file);
            return;
        }
    }
    log.info("Reading httpclient parameters from " + f.getAbsolutePath());
    Properties props = new Properties();
    try (InputStream is = new FileInputStream(f)) {
        props.load(is);
        for (Map.Entry<Object, Object> me : props.entrySet()) {
            String key = (String) me.getKey();
            String value = (String) me.getValue();
            // $NON-NLS-1$
            int typeSep = key.indexOf('$');
            try {
                if (typeSep > 0) {
                    // get past separator
                    String type = key.substring(typeSep + 1);
                    String name = key.substring(0, typeSep);
                    log.info("Defining " + name + " as " + value + " (" + type + ")");
                    if (type.equals("Integer")) {
                        params.setParameter(name, Integer.valueOf(value));
                    } else if (type.equals("Long")) {
                        params.setParameter(name, Long.valueOf(value));
                    } else if (type.equals("Boolean")) {
                        params.setParameter(name, Boolean.valueOf(value));
                    } else if (type.equals("HttpVersion")) {
                        // Commons HttpClient only
                        params.setVersion(name, value);
                    } else {
                        log.warn("Unexpected type: " + type + " for name " + name);
                    }
                } else {
                    log.info("Defining " + key + " as " + value);
                    params.setParameter(key, value);
                }
            } catch (Exception e) {
                log.error("Error in property: " + key + "=" + value + " " + e.toString());
            }
        }
    } catch (IOException e) {
        log.error("Problem loading properties " + e.toString());
    }
}