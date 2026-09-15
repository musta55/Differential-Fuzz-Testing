package org.apache.deltaspike.core.api.config.view.metadata;

import org.apache.deltaspike.core.api.config.view.ViewConfig;

/**
 * Descriptor which represents a concrete view (page).
 */
public interface ViewConfigDescriptor extends ConfigDescriptor<ViewConfig>
{
    /**
     * View ID of the current descriptor. The default implementation returns the same as ConfigDescriptor#getPath. For
     * the default implementation (and default integration with JSF) it's in place to provide a straightforward API.
     * However, e.g. an integration for a different view technology can use it e.g. for a logical id.
     *
     * @return current view ID
     */
    String getViewId();
}