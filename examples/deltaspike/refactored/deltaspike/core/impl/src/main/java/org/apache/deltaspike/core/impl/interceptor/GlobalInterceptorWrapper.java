package org.apache.deltaspike.core.impl.interceptor;

import javax.enterprise.inject.spi.AnnotatedType;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

class GlobalInterceptorWrapper implements AnnotatedType<Object>
{
    private final AnnotatedType wrapped;
    private Map<Class<? extends Annotation>, Annotation> annotations;
    private Set<Annotation> annotationSet;

    GlobalInterceptorWrapper(AnnotatedType wrapped,
                             Annotation priorityAnnotation)
    {
        this.wrapped = wrapped;
        initializeAnnotations(wrapped.getAnnotations(), priorityAnnotation);
    }

    private void initializeAnnotations(Set<Annotation> originalAnnotationSet, Annotation priorityAnnotation)
    {
        this.annotations = new HashMap<>(originalAnnotationSet.size());
        populateAnnotationsMap(originalAnnotationSet);
        addPriorityAnnotation(priorityAnnotation);
        createAnnotationSet();
    }

    private void populateAnnotationsMap(Set<Annotation> originalAnnotationSet)
    {
        for (Annotation originalAnnotation : originalAnnotationSet)
        {
            this.annotations.put(originalAnnotation.annotationType(), originalAnnotation);
        }
    }

    private void addPriorityAnnotation(Annotation priorityAnnotation)
    {
        this.annotations.put(priorityAnnotation.annotationType(), priorityAnnotation);
    }

    private void createAnnotationSet()
    {
        this.annotationSet = new HashSet<>(this.annotations.size());
        this.annotationSet.addAll(this.annotations.values());
    }

    @Override
    public Class getJavaClass()
    {
        return wrapped.getJavaClass();
    }

    @Override
    public Set getConstructors()
    {
        return wrapped.getConstructors();
    }

    @Override
    public Set getMethods()
    {
        return wrapped.getMethods();
    }

    @Override
    public Set getFields()
    {
        return wrapped.getFields();
    }

    @Override
    public Type getBaseType()
    {
        return wrapped.getBaseType();
    }

    @Override
    public Set<Type> getTypeClosure()
    {
        return wrapped.getTypeClosure();
    }

    @Override
    public <T extends Annotation> T getAnnotation(Class<T> targetClass)
    {
        return (T) this.annotations.get(targetClass);
    }

    @Override
    public Set<Annotation> getAnnotations()
    {
        return this.annotationSet;
    }

    @Override
    public boolean isAnnotationPresent(Class<? extends Annotation> targetClass)
    {
        return this.annotations.containsKey(targetClass);
    }
}