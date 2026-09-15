package org.apache.deltaspike.core.util.metadata.builder;

import javax.enterprise.inject.spi.AnnotatedConstructor;
import java.lang.reflect.Constructor;
import java.lang.reflect.Type;
import java.util.Map;

/**
 * Implementation of {@link AnnotatedConstructor} to be used in
 * {@link org.apache.deltaspike.core.util.metadata.builder.AnnotatedTypeBuilder}
 * and other CDI life cycle events.
 */
class AnnotatedConstructorImpl<X> extends AnnotatedCallableImpl<X, Constructor<X>> implements AnnotatedConstructor<X>
{

    /**
     * Constructor
     */
    AnnotatedConstructorImpl(AnnotatedTypeImpl<X> type, Constructor<?> constructor, AnnotationStore annotations,
                                    Map<Integer, AnnotationStore> parameterAnnotations,
                                    Map<Integer, Type> typeOverrides)
    {

        super(type, (Constructor<X>) constructor, constructor.getDeclaringClass(), constructor.getParameterTypes(),
                adjustGenericTypesForInnerClasses(constructor.getGenericParameterTypes(), constructor.getParameterTypes()), 
                annotations, parameterAnnotations, null, typeOverrides);
    }

    private static Type[] adjustGenericTypesForInnerClasses(Type[] genericTypes, Class<?>[] parameterTypes)
    {
        // for inner classes genericTypes and parameterTypes can be different
        // length, this is a hack to fix this.
        // TODO: investigate this behavior further, on different JVM's and
        // compilers
        if (genericTypes.length < parameterTypes.length)
        {
            Type[] adjustedGenericTypes = new Type[parameterTypes.length];
            adjustedGenericTypes[0] = parameterTypes[0];
            for (int i = 0; i < genericTypes.length; ++i)
            {
                adjustedGenericTypes[i + 1] = genericTypes[i];
            }
            return adjustedGenericTypes;
        }
        return genericTypes;
    }

}