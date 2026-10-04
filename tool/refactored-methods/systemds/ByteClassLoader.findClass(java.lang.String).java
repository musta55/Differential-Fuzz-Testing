@Override
public Class<?> findClass(String className) throws ClassNotFoundException {
    if (classBytes != null)
        return defineClass(className, classBytes, 0, classBytes.length);
    return super.loadClass(className);
}