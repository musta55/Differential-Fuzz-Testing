@Override
public InputStream getInputStream() throws IOException {
    if (data == null) {
        throw new IOException("no data");
    }
    return new ByteArrayInputStream(data);
}