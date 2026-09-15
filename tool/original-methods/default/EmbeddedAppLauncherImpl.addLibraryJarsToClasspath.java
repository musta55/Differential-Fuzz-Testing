private void addLibraryJarsToClasspath(LogicalPlan lp) throws MalformedURLException {
    String libJarsCsv = lp.getAttributes().get(Context.DAGContext.LIBRARY_JARS);
    if (libJarsCsv != null && libJarsCsv.length() != 0) {
        String[] split = libJarsCsv.split(StramClient.LIB_JARS_SEP);
        if (split.length != 0) {
            URL[] urlList = new URL[split.length];
            for (int i = 0; i < split.length; i++) {
                File file = new File(split[i]);
                urlList[i] = file.toURI().toURL();
            }
            // Set class loader.
            ClassLoader prevCl = Thread.currentThread().getContextClassLoader();
            URLClassLoader cl = URLClassLoader.newInstance(urlList, prevCl);
            Thread.currentThread().setContextClassLoader(cl);
        }
    }
}