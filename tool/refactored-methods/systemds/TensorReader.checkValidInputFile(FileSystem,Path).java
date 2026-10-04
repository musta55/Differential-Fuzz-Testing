protected static void checkValidInputFile(FileSystem fs, Path path) throws IOException {
    checkFileExists(fs, path);
    checkFileNotEmpty(fs, path);
}
// ---- helper method(s) introduced by the refactoring ----
private static void checkFileExists(FileSystem fs, Path path) throws IOException {
    if (!fs.exists(path))
        throw new IOException("File " + path.toString() + " does not exist on HDFS/LFS.");
}

private static void checkFileNotEmpty(FileSystem fs, Path path) throws IOException {
    if (HDFSTool.isFileEmpty(fs, path))
        throw new EOFException("Empty input file " + path.toString() + ".");
}

