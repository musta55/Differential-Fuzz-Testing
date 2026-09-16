package org.apache.deltaspike.core.spi.config.view;

import java.lang.annotation.Annotation;
import java.util.List;

/**
 * Allows to customize the inheritance-strategy for meta-data.
 * E.g. inheritance via std. java inheritance vs. inheritance via nested interfaces.
 * Use {@link ViewConfigRoot} to configure a custom inheritance-strategy.
 */
public interface ViewConfigInheritanceStrategy
{
    /**
     * @param viewConfigNode current view-config node
     * @return annotation instances which should be merged with the annotation instances of the node itself
     */
    List<Annotation> resolveInheritedMetaData(ViewConfigNode viewConfigNode);
}