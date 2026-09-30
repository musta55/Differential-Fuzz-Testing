public byte[] readFullFileContent(Path path) throws IOException {
    DataInputStream is = new DataInputStream(fileSystem.open(path));
    byte[] bytes = new byte[is.available()];
    try {
        is.readFully(bytes);
    } finally {
        is.close();
    }
    return bytes;
}