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
        Files.createDirectories(newDir);
    } catch (FileAlreadyExistsException ex) {
        LOGGER.info("Copying folder from '{}' to '{}', got message: " + "{}, found non empty folder with following content {}, will be ignored", file, newDir, ex.getMessage(), newDir.toFile().listFiles());
    }
    return FileVisitResult.CONTINUE;
}
// ---- helper method(s) introduced by the refactoring ----
private void processTemplateFile(Path file) throws IOException {
    // Process template file
    String templatePath = source.relativize(file).toString();
    Template template = configuration.getTemplate(templatePath);
    Path newPath = target.resolve(FilenameUtils.removeExtension(templatePath));
    try (BufferedWriter bufferedWriter = createBufferedWriter(newPath)) {
        template.process(data, bufferedWriter);
    } catch (TemplateException ex) {
        throw new IOException(ex);
    }
}

private void copyRegularFile(Path file) throws IOException {
    // Copy regular file
    Path newFile = target.resolve(source.relativize(file));
    Files.copy(file, newFile, StandardCopyOption.REPLACE_EXISTING);
}

private static BufferedWriter createBufferedWriter(Path path) throws IOException {
    FileOutputStream stream = new FileOutputStream(path.toString());
    OutputStreamWriter writer = new OutputStreamWriter(stream, StandardCharsets.UTF_8);
    return new BufferedWriter(writer);
}

