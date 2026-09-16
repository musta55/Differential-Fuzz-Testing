/*
     * (non-Javadoc)
     *
     * @see java.nio.file.SimpleFileVisitor#visitFile(java.lang.Object,
     * java.nio.file.attribute.BasicFileAttributes)
     */
@Override
public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
    // Depending on file extension, copy or process file
    String extension = FilenameUtils.getExtension(file.toString());
    if (TEMPLATED_FILE_EXT.equalsIgnoreCase(extension)) {
        processTemplateFile(file);
    } else {
        copyRegularFile(file);
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

