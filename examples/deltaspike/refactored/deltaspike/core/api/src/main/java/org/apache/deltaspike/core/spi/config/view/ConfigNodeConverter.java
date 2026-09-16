package org.apache.deltaspike.core.spi.config.view;

import org.apache.deltaspike.core.api.config.view.metadata.ConfigDescriptor;

/**
 * Allows to provide multiple strategies to process the nodes of the built config-tree.
 * Use {@link ViewConfigRoot} to configure a custom converter.
 */
public interface ConfigNodeConverter
{
    /**
     * Converts a {@link ViewConfigNode} created during the scanning process to the final {@link ConfigDescriptor}
     * used at runtime
     */
    ConfigDescriptor convert(ViewConfigNode node);
}