public ByteClassLoader(URL[] urls, ClassLoader parent, byte[] classBytes) {
    super(urls, parent);
    _classBytes = classBytes;
}