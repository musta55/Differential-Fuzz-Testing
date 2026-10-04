protected static void checkValidInputFile(FileSystem fs, Path path) throws IOException {
    //check non-existing file
    if (!fs.exists(path))
        throw new IOException("File " + path.toString() + " does not exist on HDFS/LFS.");
    //check for empty file
    if (HDFSTool.isFileEmpty(fs, path))
        throw new EOFException("Empty input file " + path.toString() + ".");
}