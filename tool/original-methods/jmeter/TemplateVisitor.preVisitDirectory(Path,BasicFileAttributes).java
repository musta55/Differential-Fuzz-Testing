/*
     * (non-Javadoc)
     *
     * @see java.nio.file.SimpleFileVisitor#preVisitDirectory(java.lang.Object,
     * java.nio.file.attribute.BasicFileAttributes)
     */
@Override
public FileVisitResult preVisitDirectory(Path file, BasicFileAttributes attrs) throws IOException {
    // Copy directory
    Path newDir = target.resolve(source.relativize(file));
    try {
        Files.copy(file, newDir);
    } catch (FileAlreadyExistsException ex) {
        LOGGER.info("Copying folder from '{}' to '{}', got message: " + "{}, found non empty folder with following content {}, will be ignored", file, newDir, ex.getMessage(), newDir.toFile().listFiles());
    }
    return FileVisitResult.CONTINUE;
}