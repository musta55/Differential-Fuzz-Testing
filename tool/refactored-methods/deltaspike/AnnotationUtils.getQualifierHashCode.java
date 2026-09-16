//based on org.apache.webbeans.container.BeanCacheKey#getQualifierHashCode
public static int getQualifierHashCode(Annotation annotation) {
    Class annotationClass = annotation.annotationType();
    int hashCode = getTypeHashCode(annotationClass);
    for (Method member : annotationClass.getDeclaredMethods()) {
        if (member.isAnnotationPresent(Nonbinding.class)) {
            continue;
        }
        final Object annotationMemberValue = ReflectionUtils.invokeMethod(annotation, member, Object.class, true);
        final int arrayValue = getArrayValue(annotationMemberValue);
        hashCode = 29 * hashCode + arrayValue;
        hashCode = 29 * hashCode + member.getName().hashCode();
    }
    return hashCode;
}
// ---- helper method(s) introduced by the refactoring ----
private static int getArrayValue(Object annotationMemberValue) {
    if (annotationMemberValue == null) {
        return 0;
    } else if (annotationMemberValue.getClass().isArray()) {
        return getArrayHashCode(annotationMemberValue);
    } else {
        return annotationMemberValue.hashCode();
    }
}

private static int getArrayHashCode(Object array) {
    Class<?> componentType = array.getClass().getComponentType();
    if (componentType.isPrimitive()) {
        return getPrimitiveArrayHashCode(array, componentType);
    } else {
        return Arrays.hashCode((Object[]) array);
    }
}

private static int getPrimitiveArrayHashCode(Object array, Class<?> componentType) {
    if (Long.TYPE == componentType) {
        return Arrays.hashCode((long[]) array);
    } else if (Integer.TYPE == componentType) {
        return Arrays.hashCode((int[]) array);
    } else if (Short.TYPE == componentType) {
        return Arrays.hashCode((short[]) array);
    } else if (Double.TYPE == componentType) {
        return Arrays.hashCode((double[]) array);
    } else if (Float.TYPE == componentType) {
        return Arrays.hashCode((float[]) array);
    } else if (Boolean.TYPE == componentType) {
        return Arrays.hashCode((boolean[]) array);
    } else if (Byte.TYPE == componentType) {
        return Arrays.hashCode((byte[]) array);
    } else if (Character.TYPE == componentType) {
        return Arrays.hashCode((char[]) array);
    } else {
        return 0;
    }
}

