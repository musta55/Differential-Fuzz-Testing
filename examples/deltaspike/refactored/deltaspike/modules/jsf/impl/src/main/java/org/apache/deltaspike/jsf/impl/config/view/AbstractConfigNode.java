package org.apache.deltaspike.jsf.impl.config.view;

import org.apache.deltaspike.core.api.config.view.metadata.CallbackDescriptor;
import org.apache.deltaspike.core.spi.config.view.ViewConfigNode;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public abstract class AbstractConfigNode implements ViewConfigNode
{
    private final ViewConfigNode parent;
    private List<ViewConfigNode> children = new ArrayList<ViewConfigNode>();
    private Set<Annotation> metaData;
    private List<Annotation> inheritedMetaData = new ArrayList<Annotation>();
    private Map<Class<? extends Annotation>, List<CallbackDescriptor>> callbackDescriptors =
        new HashMap<Class<? extends Annotation>, List<CallbackDescriptor>>();

    protected AbstractConfigNode(ViewConfigNode parent, Set<Annotation> metaData)
    {
        this.parent = parent;
        this.metaData = new HashSet<Annotation>(metaData); //might be read-only (from Annotated#getAnnotations)
    }

    @Override
    public ViewConfigNode getParent()
    {
        return this.parent;
    }

    @Override
    public List<ViewConfigNode> getChildren()
    {
        return this.children;
    }

    @Override
    public Set<Annotation> getMetaData()
    {
        return this.metaData;
    }

    @Override
    public List<Annotation> getInheritedMetaData()
    {
        return this.inheritedMetaData;
    }

    @Override
    public Map<Class<? extends Annotation>, List<CallbackDescriptor>> getCallbackDescriptors()
    {
        return this.callbackDescriptors;
    }

    @Override
    public List<CallbackDescriptor> getCallbackDescriptors(Class<? extends Annotation> metaDataType)
    {
        List<CallbackDescriptor> result = this.callbackDescriptors.get(metaDataType);

        if (result == null)
        {
            result = new ArrayList<CallbackDescriptor>();
            this.callbackDescriptors.put(metaDataType, result);
        }
        return result;
    }

    @Override
    public void registerCallbackDescriptors(Class<? extends Annotation> metaDataType,
                                            CallbackDescriptor callbackDescriptor)
    {
        if (!callbackDescriptor.getCallbackMethods().isEmpty())
        {
            getCallbackDescriptors(metaDataType).add(callbackDescriptor);
        }
    }
}