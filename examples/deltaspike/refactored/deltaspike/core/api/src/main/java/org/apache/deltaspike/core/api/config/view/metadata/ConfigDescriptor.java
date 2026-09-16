package org.apache.deltaspike.core.api.config.view.metadata;

import java.lang.annotation.Annotation;
import java.util.List;

/**
 * Base descriptor for all type-safe view-configs which describes the config class, metadata, callbacks and other
 * properties of a view-config.
 *
 * @param <CT> class of the view-config
 */
public interface ConfigDescriptor<CT>
{
    Class<? extends CT> getConfigClass();

    /**
     * Metadata configured for this view-config. Resolves {@link ViewMetaData}-annotated annotations which are inherited
     * or directly present on the view-config class.
     *
     * @return metadata of this view-config
     */
    List<Annotation> getMetaData();

    /**
     * Metadata which is configured for this view-config. Resolves {@link ViewMetaData}-annotated annotations which are
     * inherited or directly present on the view-config class.
     *
     * @param target target type
     *
     * @return custom metadata for the given type of this view-config
     */
    <T extends Annotation> List<T> getMetaData(Class<T> target);

    /**
     * Callbacks which are configured for this view-config and bound to the given metadata type.
     *
     * @param metaDataType type of the metadata (e.g. ViewControllerRef.class)
     *
     * @return descriptor for the callback or null if there is no callback method
     */
    CallbackDescriptor getCallbackDescriptor(Class<? extends Annotation> metaDataType);

    /**
     * Callbacks which are configured for this view-config and bound to the given metadata type.
     *
     * @param metaDataType type of the metadata (e.g. ViewControllerRef.class)
     * @param callbackType type of the callback (e.g. PreRenderView.class)
     *
     * @return descriptor for the callback null if there is no callback-method
     */
    CallbackDescriptor getCallbackDescriptor(Class<? extends Annotation> metaDataType,
                                             Class<? extends Annotation> callbackType);

    /**
     * Callbacks which are configured for this view-config and bound to the given metadata type.
     *
     * @param metaDataType type of the metadata (e.g. ViewControllerRef.class)
     * @param executorType type of the executor which returns a typed result (e.g. Secured.Descriptor)
     *
     * @return executable descriptor for the callback or null if there is no callback method
     */
    @SuppressWarnings("rawtypes")
    //TODO <T extends ExecutableCallbackDescriptor<?>> when major version is incremented
    <T extends ExecutableCallbackDescriptor> T getExecutableCallbackDescriptor(Class<? extends Annotation> metaDataType,
                                                                               Class<? extends T> executorType);

    /**
     * Callbacks which are configured for this view-config and bound to the given metadata type.
     *
     * @param metaDataType type of the metadata (e.g. ViewControllerRef.class)
     * @param callbackType type of the callback (e.g. PreRenderView.class)
     * @param executorType type of the executor which returns a typed result (e.g. Secured.Descriptor)
     *
     * @return executable descriptor for the callback or null if there is no callback method
     */
    @SuppressWarnings("rawtypes")
    //TODO <T extends ExecutableCallbackDescriptor<?>> when major version is incremented
    <T extends ExecutableCallbackDescriptor> T getExecutableCallbackDescriptor(Class<? extends Annotation> metaDataType,
                                                                               Class<? extends Annotation> callbackType,
                                                                               Class<? extends T> executorType);

    /**
     * Returns the string representation of the resource (page, folder) represented by this view-config.
     *
     * @return relative path to the folder or page
     */
    String getPath();
}