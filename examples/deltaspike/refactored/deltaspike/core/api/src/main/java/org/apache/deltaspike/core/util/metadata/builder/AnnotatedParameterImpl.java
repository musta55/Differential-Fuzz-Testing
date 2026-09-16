package org.apache.deltaspike.core.util.metadata.builder;

import javax.enterprise.inject.spi.AnnotatedCallable;
import javax.enterprise.inject.spi.AnnotatedParameter;
import java.lang.reflect.Type;

/**
 * Implementation of {@link AnnotatedParameter}.
 */
class AnnotatedParameterImpl<X> extends AnnotatedImpl implements AnnotatedParameter<X>
{
    private final int position;
    private final AnnotatedCallable<X> declaringCallable;

    /**
     * Constructor
     */
    AnnotatedParameterImpl(AnnotatedCallable<X> declaringCallable, Class<?> type, int position,
                                  AnnotationStore annotations, Type genericType, Type typeOverride)
    {
        super(type, annotations, genericType, typeOverride);
        this.declaringCallable = declaringCallable;
        this.position = position;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AnnotatedCallable<X> getDeclaringCallable()
    {
        return declaringCallable;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getPosition()
    {
        return position;
    }

    /**
     * Builder pattern for AnnotatedParameterImpl
     */
    public static class Builder<X>
    {
        private AnnotatedCallable<X> declaringCallable;
        private Class<?> type;
        private int position;
        private AnnotationStore annotations;
        private Type genericType;
        private Type typeOverride;

        public Builder<X> declaringCallable(AnnotatedCallable<X> declaringCallable)
        {
            this.declaringCallable = declaringCallable;
            return this;
        }

        public Builder<X> type(Class<?> type)
        {
            this.type = type;
            return this;
        }

        public Builder<X> position(int position)
        {
            this.position = position;
            return this;
        }

        public Builder<X> annotations(AnnotationStore annotations)
        {
            this.annotations = annotations;
            return this;
        }

        public Builder<X> genericType(Type genericType)
        {
            this.genericType = genericType;
            return this;
        }

        public Builder<X> typeOverride(Type typeOverride)
        {
            this.typeOverride = typeOverride;
            return this;
        }

        public AnnotatedParameterImpl<X> build()
        {
            return new AnnotatedParameterImpl<>(declaringCallable, type, position, annotations, genericType, typeOverride);
        }
    }
}