public FastClassIndexReader(final InputStream is) throws IOException {
    initializeBuffer(is);
    parseConstantPool();
    extractClassMetadata();
}
// ---- helper method(s) introduced by the refactoring ----
/**
 * Read class file content into shared buffer from input stream
 * Stream won't be closed
 * @param is
 * @throws IOException
 */
private void initializeBuffer(InputStream is) throws IOException {
    if (is == null) {
        throw new IOException("Class not found");
    }
    bSize = 0;
    while (true) {
        int n = is.read(b, bSize, b.length - bSize);
        if (n == -1) {
            break;
        }
        bSize += n;
        if (bSize >= b.length) {
            byte[] c = new byte[b.length << 2];
            System.arraycopy(b, 0, c, 0, b.length);
            b = c;
        }
    }
}

/**
 * read and index the constant pool section for getting class metadata later
 */
private void parseConstantPool() {
    // checks the class version
    if (readShort(6) > Opcodes.V1_8) {
        throw new IllegalArgumentException();
    }
    // parses the constant pool
    items = new int[readUnsignedShort(8)];
    int n = items.length;
    int index = 10;
    for (int i = 1; i < n; ++i) {
        items[i] = index + 1;
        index += getConstantPoolItemSize(index);
    }
    // the class header information starts just after the constant pool
    header = index;
}

private int getConstantPoolItemSize(int index) {
    switch(b[index]) {
        case FIELD:
        case METH:
        case IMETH:
        case INT:
        case FLOAT:
        case NAME_TYPE:
        case INDY:
            return 5;
        case LONG:
        case DOUBLE:
            return 9;
        case UTF8:
            return 3 + readUnsignedShort(index + 1);
        case HANDLE:
            return 4;
        default:
            return 3;
    }
}

/**
 * read class metadata, class name, parent name, interfaces name, is instantiable(has public non-arg constructor)
 * or not
 * @throws UnsupportedEncodingException
 */
private void extractClassMetadata() throws UnsupportedEncodingException {
    // reads the class declaration
    int u = header;
    int access = readUnsignedShort(u);
    isInstantiable = ASMUtil.isPublic(access) && !ASMUtil.isAbstract(access);
    name = readClass(u + 2);
    superName = readClass(u + 4);
    interfaces = readInterfaces(u + 6);
    u += 8 + interfaces.length * 2;
    if (!isInstantiable) {
        return;
    }
    // reads the constructor
    skipFields(u);
    u += 2;
    isInstantiable = hasDefaultConstructor(u);
}

private String[] readInterfaces(int index) throws UnsupportedEncodingException {
    int interfaceCount = readUnsignedShort(index);
    String[] interfaces = new String[interfaceCount];
    for (int i = 0; i < interfaceCount; ++i) {
        interfaces[i] = readClass(index + 2 + i * 2);
    }
    return interfaces;
}

private void skipFields(int u) {
    for (int i = readUnsignedShort(u); i > 0; --i) {
        skipFieldAttributes(u + 8);
        u += 8;
    }
}

private void skipFieldAttributes(int u) {
    for (int j = readUnsignedShort(u); j > 0; --j) {
        u += 6 + readInt(u + 12);
    }
}

private boolean hasDefaultConstructor(int u) throws UnsupportedEncodingException {
    for (int i = readUnsignedShort(u); i > 0; --i) {
        if (isDefaultConstructor(u + 2)) {
            return true;
        }
        skipMethodAttributes(u + 8);
        u += 8;
    }
    return false;
}

private void skipMethodAttributes(int u) {
    for (int j = readUnsignedShort(u); j > 0; --j) {
        u += 6 + readInt(u + 12);
    }
}

