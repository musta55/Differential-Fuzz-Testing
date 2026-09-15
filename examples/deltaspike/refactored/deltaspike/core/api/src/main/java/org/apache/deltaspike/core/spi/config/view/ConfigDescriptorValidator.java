package org.apache.deltaspike.core.spi.config.view;

import org.apache.deltaspike.core.api.config.view.metadata.ConfigDescriptor;

/**
 * Allows to validate the final view-config descriptors before they get deployed.
 * Since the config-descriptor contains e.g. the final path,
 * it's also possible to validate if the corresponding file exists.
 * Use {@link ViewConfigRoot} to configure 1-n validators.
 */
public interface ConfigDescriptorValidator
{
    /**
     * Validates the given config-descriptor
     * @param configDescriptor (merged) config-descriptor directly before it gets deployed
     * @return true if the descriptor is valid, false otherwise
     */
    boolean isValid(ConfigDescriptor configDescriptor);
}