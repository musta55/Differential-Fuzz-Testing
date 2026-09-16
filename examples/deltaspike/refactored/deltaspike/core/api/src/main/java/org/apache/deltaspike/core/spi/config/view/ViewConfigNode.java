package org.apache.deltaspike.core.spi.config.view;

import org.apache.deltaspike.core.api.config.view.metadata.CallbackDescriptor;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Node-type used for building the meta-data-tree during the bootstrapping process.
 */
public interface ViewConfigNode
{
    ViewConfigNode getParent();

    List<ViewConfigNode> getChildren();

    Class<?> getSource();

    Set<Annotation> getMetaData();

    List<Annotation> getInheritedMetaData();

    Map<Class<? extends Annotation>, List<CallbackDescriptor>> getCallbackDescriptors();

    //TODO
    List<CallbackDescriptor> getCallbackDescriptors(Class<? extends Annotation> metaDataType);

    void registerCallbackDescriptors(Class<? extends Annotation> metaDataType, CallbackDescriptor callbackDescriptor);
}