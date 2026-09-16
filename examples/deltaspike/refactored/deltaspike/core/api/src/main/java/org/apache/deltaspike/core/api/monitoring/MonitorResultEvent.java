package org.apache.deltaspike.core.api.monitoring;

import javax.enterprise.inject.Typed;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * This class will be used as event to transport final monitor values
 *
 * @see InvocationMonitored
 */
@Typed()
public class MonitorResultEvent
{
    private final Map<String, AtomicInteger> methodInvocations;
    private final Map<String, AtomicInteger> classInvocations;
    private final Map<String, AtomicLong> methodDurations;

    public MonitorResultEvent(Map<String, AtomicInteger> methodInvocations,
                              Map<String, AtomicInteger> classInvocations,
                              Map<String, AtomicLong> methodDurations)
    {
        this.methodInvocations = new HashMap<>(methodInvocations);
        this.classInvocations = new HashMap<>(classInvocations);
        this.methodDurations = new HashMap<>(methodDurations);
    }

    /**
     * @return Map with Counters for all method invocations
     * key = fully qualified method name (includes class)
     * value = AtomicInteger with invocation count value
     */
    public Map<String, AtomicInteger> getMethodInvocations()
    {
        return new HashMap<>(methodInvocations);
    }

    /**
     * @return Map with Counter for all class invocations
     * key = fully qualified class name
     * value = AtomicInteger with invocation count value
     */
    public Map<String, AtomicInteger> getClassInvocations()
    {
        return new HashMap<>(classInvocations);
    }

    /**
     * @return Map with duration for all method invocations
     * key = fully qualified method name (includes class)
     * value = AtomicLong with duration nanos
     */
    public Map<String, AtomicLong> getMethodDurations()
    {
        return new HashMap<>(methodDurations);
    }
}