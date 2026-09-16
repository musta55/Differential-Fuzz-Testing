/**
 * Adapted from http://asm.ow2.org/doc/faq.html#Q5
 *
 * @param b
 *
 * @return Class<?>
 */
static Class<?> defineClassClassLoader(ClassLoader loader, String className, byte[] b, Class<?> originalClass, ProtectionDomain protectionDomain) {
    try {
        return (Class<?>) CLASS_LOADER_DEFINE_CLASS.invoke(loader, className, b, Integer.valueOf(0), Integer.valueOf(b.length), protectionDomain);
    } catch (Exception e) {
        throw e instanceof RuntimeException ? ((RuntimeException) e) : new RuntimeException(e);
    }
}