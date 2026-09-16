package org.apache.deltaspike.core.spi.config.view;

import org.apache.deltaspike.core.api.config.view.ViewConfig;

import java.lang.annotation.Annotation;

/**
 * It's restricted to reference {@link ViewConfig} classes to force more solid references.
 * (This restriction is intended.)
 * To reference folder-nodes, it's needed that the corresponding config-class implements {@link ViewConfig} as well.
 *
 * It's used instead of a marker annotation to be more flexible (e.g. for special cases like conditional references).
 *
 * @param <T> type of the annotation which provides the information about the target view-config/s
 */
public interface TargetViewConfigProvider<T extends Annotation>
{
    /**
     * Retrieves the target view configurations based on the provided metadata annotation.
     *
     * @param inlineMetaData the annotation providing metadata about the target view-configs
     * @return an array of classes implementing {@link ViewConfig} representing the target view-configs
     */
    Class<? extends ViewConfig>[] getTarget(T inlineMetaData);
}