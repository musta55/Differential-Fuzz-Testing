public DiskStorage() throws IOException {
    File tempFile = File.createTempFile("msp", "msp");
    basePath = tempFile.getParent();
    tempFile.delete();
    logger.info("using {} as the basepath for spooling.", basePath);
}