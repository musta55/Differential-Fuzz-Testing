package org.apache.deltaspike.core.spi.config.view;

import java.lang.annotation.Annotation;

/**
 * Allows to transform an annotation annotated with @InlineViewMetaData to an annotation annotated with @ViewMetaData.
 * This transformer is optional and only needed if it should result in the same at runtime, but the inline-meta-data
 * needs a different syntax via a different annotation (compared to the view-config meta-data).
 * E.g. see @ViewRef vs. @ViewControllerRef.
 *
 * @param <I> type of the inline-meta-data
 * @param <T> type of the target-meta-data
 */
public interface InlineMetaDataTransformer<I extends Annotation, T extends Annotation>
{
    T convertToViewMetaData(I inlineMetaData, Class<?> sourceClass);
}