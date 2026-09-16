package org.apache.deltaspike.core.api.config;

import java.io.Serializable;

/**
 * Marker interface for all classes used for dynamic configuration of DeltaSpike itself. The term <i>Dynamic
 * configuration</i> refers to values which can be determined and changed during runtime and shouldn't be accessed
 * during container boot time.
 *
 * <p>
 * All DeltaSpike dynamic configuration objects implement this interface so they can be found more easily. There is no
 * other functionality implied with this interface.</p>
 *
 * <p>
 * DeltaSpike uses a <i>type-safe configuration</i> approach for most internal configuration. Instead of writing a
 * properties file or XML, you just implement one of the configuration interfaces which will then be picked up as a
 * CDI bean. If* there is already a default configuration for some functionality in DeltaSpike, you can use &#064;
 * Specializes or &#064;Alternative to change those.</p>
 *
 * <p>
 * See {@link org.apache.deltaspike.core.api.config.base.DeltaSpikeBaseConfig} for static DeltaSpike configuration
 * based on properties.</p>
 *
 */
public interface DeltaSpikeConfig extends Serializable
{
}