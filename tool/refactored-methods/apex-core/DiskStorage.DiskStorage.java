public DiskStorage() throws IOException {
    Path tempDir = Files.createTempDirectory("msp");
    basePath = tempDir.toString();
    logger.info("Using {} as the basepath for spooling.", basePath);
}