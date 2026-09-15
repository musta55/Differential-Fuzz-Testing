package org.apache.deltaspike.core.impl.util;

import org.apache.deltaspike.core.util.ClassUtils;
import org.apache.deltaspike.core.util.metadata.AnnotationInstanceProvider;

import javax.enterprise.inject.Typed;
import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;

@Typed()
public abstract class AnnotationInstanceUtils
{
    private AnnotationInstanceUtils()
    {
        // prevent instantiation
    }

    /**
     * @return a new instance of {@link javax.annotation.Priority} with the given value
     *         if the annotation-class is available in a cdi 1.1+ based environment, null otherwise
     */
    public static Annotation getPriorityAnnotationInstance(int priorityValue)
    {
        Annotation priorityAnnotationInstance = null;

        Class<? extends Annotation> priorityAnnotationClass =
            ClassUtils.tryToLoadClassForName("javax.annotation.Priority");

        //check for @Priority and CDI v1.1+
        if (priorityAnnotationClass != null &&
            ClassUtils.tryToLoadClassForName("javax.enterprise.inject.spi.AfterTypeDiscovery") != null)
        {
            Map<String, Object> defaultValueMap = createDefaultValueMap(priorityValue);
            priorityAnnotationInstance = AnnotationInstanceProvider.of(priorityAnnotationClass, defaultValueMap);
        }

        return priorityAnnotationInstance;
    }

    private static Map<String, Object> createDefaultValueMap(int priorityValue)
    {
        Map<String, Object> defaultValueMap = new HashMap<>();
        defaultValueMap.put("value", priorityValue);
        return defaultValueMap;
    }
}