package org.apache.deltaspike.core.util;

import javax.enterprise.inject.Typed;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.util.Nonbinding;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Arrays;

@Typed()
public abstract class AnnotationUtils
{
    private AnnotationUtils()
    {
        // prevent instantiation
    }

    public static <T extends Annotation> T extractAnnotationFromMethodOrClass(
        BeanManager beanManager, Method targetMethod, Class targetClass, Class<T> targetAnnotationType)
    {
        T result = extractAnnotationFromMethod(beanManager, targetMethod, targetAnnotationType);

        if (result == null)
        {
            //see DELTASPIKE-517
            Class unproxiedTargetClass = ProxyUtils.getUnproxiedClass(targetClass);

            // and if not found search on the class
            result = findAnnotation(beanManager, unproxiedTargetClass.getAnnotations(), targetAnnotationType);
        }
        return result;
    }

    public static <T extends Annotation> T extractAnnotationFromMethod(
        BeanManager beanManager, Method targetMethod, Class<T> targetAnnotationType)
    {
        return findAnnotation(beanManager, targetMethod.getAnnotations(), targetAnnotationType);
    }

    public static  <T extends Annotation> T findAnnotation(
            BeanManager beanManager, Annotation[] annotations, Class<T> targetAnnotationType)
    {
        for (Annotation annotation : annotations)
        {
            if (targetAnnotationType.equals(annotation.annotationType()))
            {
                return (T) annotation;
            }
            if (beanManager.isStereotype(annotation.annotationType()))
            {
                T result = findAnnotation(
                        beanManager, annotation.annotationType().getAnnotations(), targetAnnotationType);
                if (result != null)
                {
                    return result;
                }
            }
        }
        return null;
    }

    //based on org.apache.webbeans.container.BeanCacheKey#getQualifierHashCode
    public static int getQualifierHashCode(Annotation annotation)
    {
        Class annotationClass = annotation.annotationType();

        int hashCode = getTypeHashCode(annotationClass);

        for (Method member : annotationClass.getDeclaredMethods())
        {
            if (member.isAnnotationPresent(Nonbinding.class))
            {
                continue;
            }

            final Object annotationMemberValue = ReflectionUtils.invokeMethod(annotation, member, Object.class, true);

            final int arrayValue = getArrayValue(annotationMemberValue);

            hashCode = 29 * hashCode + arrayValue;
            hashCode = 29 * hashCode + member.getName().hashCode();
        }

        return hashCode;
    }

    private static int getArrayValue(Object annotationMemberValue)
    {
        if (annotationMemberValue == null)
        {
            return 0;
        }
        else if (annotationMemberValue.getClass().isArray())
        {
            return getArrayHashCode(annotationMemberValue);
        }
        else
        {
            return annotationMemberValue.hashCode();
        }
    }

    private static int getArrayHashCode(Object array)
    {
        Class<?> componentType = array.getClass().getComponentType();
        if (componentType.isPrimitive())
        {
            return getPrimitiveArrayHashCode(array, componentType);
        }
        else
        {
            return Arrays.hashCode((Object[]) array);
        }
    }

    private static int getPrimitiveArrayHashCode(Object array, Class<?> componentType)
    {
        if (Long.TYPE == componentType)
        {
            return Arrays.hashCode((long[]) array);
        }
        else if (Integer.TYPE == componentType)
        {
            return Arrays.hashCode((int[]) array);
        }
        else if (Short.TYPE == componentType)
        {
            return Arrays.hashCode((short[]) array);
        }
        else if (Double.TYPE == componentType)
        {
            return Arrays.hashCode((double[]) array);
        }
        else if (Float.TYPE == componentType)
        {
            return Arrays.hashCode((float[]) array);
        }
        else if (Boolean.TYPE == componentType)
        {
            return Arrays.hashCode((boolean[]) array);
        }
        else if (Byte.TYPE == componentType)
        {
            return Arrays.hashCode((byte[]) array);
        }
        else if (Character.TYPE == componentType)
        {
            return Arrays.hashCode((char[]) array);
        }
        else
        {
            return 0;
        }
    }

    private static int getTypeHashCode(Type type)
    {
        int typeHash = type.hashCode();
        if (typeHash == 0 && type instanceof Class)
        {
            return ((Class)type).getName().hashCode();
        }

        return typeHash;
    }
}