package org.apache.deltaspike.core.api.config.view.metadata;

import org.apache.deltaspike.core.api.config.view.ViewConfig;

import java.util.List;

/**
 * Resolver of view-configs.
 *
 * A {@link ConfigDescriptor} can be bound to any config class (without required base type). That's needed e.g. for
 * folder-configs. Whereas {@link ViewConfigDescriptor}s only represent classes which inherit from {@link ViewConfig}
 * which is required for all view-configs.
 *
 * Use {@link org.apache.deltaspike.core.spi.config.view.ViewConfigRoot} to register a custom resolver.
 */
//TODO re-visit name since we also need ConfigDescriptor
public interface ViewConfigResolver
{
    ConfigDescriptor<?> getConfigDescriptor(String path);

    /**
     * Resolves the {@link ConfigDescriptor} for the given class.
     *
     * @param configClass config class which usually represents a folder node
     *
     * @return config descriptor which represents the given config class
     */
    ConfigDescriptor<?> getConfigDescriptor(Class<?> configClass);

    //TODO re-visit name (depends on other discussions)
    /**
     * Resolves all descriptors for folders.
     *
     * @return all descriptors for the known folder-configs
     */
    List<ConfigDescriptor<?>> getConfigDescriptors();

    /**
     * Resolves the {@link ViewConfigDescriptor} for the given view-id.
     *
     * @param viewId view-id of the page
     *
     * @return view-config descriptor which represents the given view-id, null otherwise
     */
    ViewConfigDescriptor getViewConfigDescriptor(String viewId);

    /**
     * Resolves the {@link ViewConfigDescriptor} for the given view-config class.
     *
     * @param viewDefinitionClass view-config class of the page
     *
     * @return view-config descriptor which represents the given view-config class
     */
    ViewConfigDescriptor getViewConfigDescriptor(Class<? extends ViewConfig> viewDefinitionClass);

    /**
     * Resolves all descriptors for the known {@link ViewConfig}s.
     *
     * @return all descriptors for the known view-configs
     */
    List<ViewConfigDescriptor> getViewConfigDescriptors();

    /**
     * Resolves the descriptor for the default error page.
     *
     * @return descriptor for the default error page
     */
    ViewConfigDescriptor getDefaultErrorViewConfigDescriptor();
}