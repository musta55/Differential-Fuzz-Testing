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
        // Process template file
        String templatePath = source.relativize(file).toString();
        Template template = configuration.getTemplate(templatePath);
        Path newPath = target.resolve(FilenameUtils.removeExtension(templatePath));
        try (FileOutputStream stream = new FileOutputStream(newPath.toString());
            Writer writer = new OutputStreamWriter(stream, StandardCharsets.UTF_8);
            BufferedWriter bufferedWriter = new BufferedWriter(writer)) {
            template.process(data, bufferedWriter);
        } catch (TemplateException ex) {
            throw new IOException(ex);
        }
    } else {
        // Copy regular file
        Path newFile = target.resolve(source.relativize(file));
        Files.copy(file, newFile, StandardCopyOption.REPLACE_EXISTING);
    }
    return FileVisitResult.CONTINUE;
}