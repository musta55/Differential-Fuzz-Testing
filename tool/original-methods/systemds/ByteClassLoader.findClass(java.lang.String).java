@Override
public Class<?> findClass(String className) throws ClassNotFoundException {
    if (_classBytes != null)
        return defineClass(className, _classBytes, 0, _classBytes.length);
    return super.loadClass(className);
}