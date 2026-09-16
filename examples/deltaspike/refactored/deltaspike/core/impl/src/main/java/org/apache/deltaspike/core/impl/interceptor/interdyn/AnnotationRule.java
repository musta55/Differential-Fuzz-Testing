package org.apache.deltaspike.core.impl.interceptor.interdyn;

import java.lang.annotation.Annotation;

/**
 * Contains a mapping between a dynamic interceptor rule and the name of the additional annotation to be added
 */
public class AnnotationRule
{
    /**
     * A RegExp to identify the classes which should get modified
     */
    private String rule;

    /**
     * The Annotation to be added
     */
    private Annotation additionalAnnotation;

    private boolean requiresProxy;

    public AnnotationRule(String rule, Annotation interceptorBinding, boolean requiresProxy)
    {
        this.rule = rule;
        this.additionalAnnotation = interceptorBinding;
        this.requiresProxy = requiresProxy;
    }

    public String getRule()
    {
        return rule;
    }

    public Annotation getAdditionalAnnotation()
    {
        return additionalAnnotation;
    }

    public boolean requiresProxy()
    {
        return requiresProxy;
    }
}