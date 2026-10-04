public ByteClassLoader(URL[] urls, ClassLoader parent, byte[] classBytes) {
    super(urls, parent);
    this.classBytes = classBytes;
}