package org.apache.deltaspike.core.util.metadata.builder;

import org.apache.deltaspike.core.util.HierarchyDiscovery;

import javax.enterprise.inject.spi.Annotated;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * The base class for all New Annotated types.
 */
abstract class AnnotatedImpl implements Annotated
{
    private final Type type;
    private final Set<Type> typeClosure;
    private final AnnotationStore annotations;

    protected AnnotatedImpl(Class<?> type, AnnotationStore annotations, Type genericType, Type overriddenType)
    {
        this.type = initializeType(type, genericType, overriddenType);
        this.typeClosure = initializeTypeClosure(type, genericType, overriddenType);
        this.annotations = initializeAnnotations(annotations);
    }

    private Type initializeType(Class<?> type, Type genericType, Type overriddenType)
    {
        if (overriddenType != null)
        {
            return overriddenType;
        }
        return genericType != null ? genericType : type;
    }

    private Set<Type> initializeTypeClosure(Class<?> type, Type genericType, Type overriddenType)
    {
        if (overriddenType != null)
        {
            return Collections.singleton(overriddenType);
        }
        return new HierarchyDiscovery(genericType != null ? genericType : type).getTypeClosure();
    }

    private AnnotationStore initializeAnnotations(AnnotationStore annotations)
    {
        return annotations != null ? annotations : new AnnotationStore();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T extends Annotation> T getAnnotation(Class<T> annotationType)
    {
        return annotations.getAnnotation(annotationType);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<Annotation> getAnnotations()
    {
        return annotations.getAnnotations();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isAnnotationPresent(Class<? extends Annotation> annotationType)
    {
        return annotations.isAnnotationPresent(annotationType);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<Type> getTypeClosure()
    {
        return new HashSet<>(typeClosure);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Type getBaseType()
    {
        return type;
    }
}