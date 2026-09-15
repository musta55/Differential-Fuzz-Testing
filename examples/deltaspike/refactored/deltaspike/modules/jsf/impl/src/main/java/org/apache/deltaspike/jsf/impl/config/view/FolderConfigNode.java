package org.apache.deltaspike.jsf.impl.config.view;

import org.apache.deltaspike.core.api.config.view.metadata.CallbackDescriptor;
import org.apache.deltaspike.core.spi.config.view.ViewConfigNode;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class FolderConfigNode extends AbstractConfigNode
{
    //not all interfaces have to implement the ViewConfig interface
    private final Class<?> nodeId;

    public FolderConfigNode(Class<?> nodeId, ViewConfigNode parent, Set<Annotation> nodeMetaData)
    {
        super(parent, nodeMetaData);
        this.nodeId = nodeId;
    }

    public FolderConfigNode(ViewConfigNode nodeToCopy, Class<?> viewConfigClass)
    {
        super(nodeToCopy.getParent(), nodeToCopy.getMetaData());
        getInheritedMetaData().addAll(nodeToCopy.getInheritedMetaData());
        getChildren().addAll(nodeToCopy.getChildren());

        for (Map.Entry<Class<? extends Annotation>, List<CallbackDescriptor>> callbackDescriptorEntry :
                nodeToCopy.getCallbackDescriptors().entrySet())
        {
            for (CallbackDescriptor callbackDescriptor : callbackDescriptorEntry.getValue())
            {
                registerCallbackDescriptors(callbackDescriptorEntry.getKey(), callbackDescriptor);
            }
        }
        this.nodeId = viewConfigClass;
    }

    @Override
    public Class<?> getSource()
    {
        return this.nodeId;
    }
}