//based on org.apache.webbeans.container.BeanCacheKey#getQualifierHashCode
public static int getQualifierHashCode(Annotation annotation) {
    Class annotationClass = annotation.annotationType();
    int hashCode = getTypeHashCode(annotationClass);
    for (Method member : annotationClass.getDeclaredMethods()) {
        if (member.isAnnotationPresent(Nonbinding.class)) {
            continue;
        }
        final Object annotationMemberValue = ReflectionUtils.invokeMethod(annotation, member, Object.class, true);
        final int arrayValue;
        if (annotationMemberValue == null) /*possible with literals*/
        {
            arrayValue = 0;
        } else if (annotationMemberValue.getClass().isArray()) {
            Class<?> annotationMemberType = annotationMemberValue.getClass().getComponentType();
            if (annotationMemberType.isPrimitive()) {
                if (Long.TYPE == annotationMemberType) {
                    arrayValue = Arrays.hashCode((long[]) annotationMemberValue);
                } else if (Integer.TYPE == annotationMemberType) {
                    arrayValue = Arrays.hashCode((int[]) annotationMemberValue);
                } else if (Short.TYPE == annotationMemberType) {
                    arrayValue = Arrays.hashCode((short[]) annotationMemberValue);
                } else if (Double.TYPE == annotationMemberType) {
                    arrayValue = Arrays.hashCode((double[]) annotationMemberValue);
                } else if (Float.TYPE == annotationMemberType) {
                    arrayValue = Arrays.hashCode((float[]) annotationMemberValue);
                } else if (Boolean.TYPE == annotationMemberType) {
                    arrayValue = Arrays.hashCode((boolean[]) annotationMemberValue);
                } else if (Byte.TYPE == annotationMemberType) {
                    arrayValue = Arrays.hashCode((byte[]) annotationMemberValue);
                } else if (Character.TYPE == annotationMemberType) {
                    arrayValue = Arrays.hashCode((char[]) annotationMemberValue);
                } else {
                    arrayValue = 0;
                }
            } else {
                arrayValue = Arrays.hashCode((Object[]) annotationMemberValue);
            }
        } else {
            arrayValue = annotationMemberValue.hashCode();
        }
        hashCode = 29 * hashCode + arrayValue;
        hashCode = 29 * hashCode + member.getName().hashCode();
    }
    return hashCode;
}