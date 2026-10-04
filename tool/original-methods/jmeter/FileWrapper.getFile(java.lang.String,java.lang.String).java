private static FileRowColContainer getFile(String file, String alias) throws FileNotFoundException, IOException {
    FileRowColContainer frcc;
    if ((frcc = fileContainers.get(alias)) == null) {
        frcc = new FileRowColContainer(file);
        fileContainers.put(alias, frcc);
        log.info("Saved {} as {} delimiter=<{}>", file, alias, frcc.getDelimiter());
        if (defaultFile.length() == 0) {
            // Save in case needed later
            defaultFile = file;
        }
    }
    return frcc;
}