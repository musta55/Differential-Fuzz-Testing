public byte[] readFullFileContent(Path path) throws IOException {
    try (DataInputStream is = new DataInputStream(fileSystem.open(path))) {
        byte[] bytes = new byte[is.available()];
        is.readFully(bytes);
        return bytes;
    }
}