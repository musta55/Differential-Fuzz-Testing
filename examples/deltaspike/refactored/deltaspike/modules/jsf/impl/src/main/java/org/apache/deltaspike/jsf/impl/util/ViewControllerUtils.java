package org.apache.deltaspike.jsf.impl.util;

import org.apache.deltaspike.core.api.config.view.controller.ViewControllerRef;
import org.apache.deltaspike.core.api.config.view.metadata.SimpleCallbackDescriptor;
import org.apache.deltaspike.core.api.config.view.metadata.ViewConfigDescriptor;

import java.lang.annotation.Annotation;

public abstract class ViewControllerUtils
{
    public static void executeViewControllerCallback(ViewConfigDescriptor viewDefinitionEntry,
                                                     Class<? extends Annotation> callbackType)
    {
        if (viewDefinitionEntry == null)
        {
            return;
        }

        SimpleCallbackDescriptor callbackDescriptor = getCallbackDescriptor(viewDefinitionEntry, callbackType);
        executeCallback(callbackDescriptor);
    }

    private static SimpleCallbackDescriptor getCallbackDescriptor(ViewConfigDescriptor viewDefinitionEntry,
                                                                 Class<? extends Annotation> callbackType)
    {
        return viewDefinitionEntry.getExecutableCallbackDescriptor(
                ViewControllerRef.class, callbackType, SimpleCallbackDescriptor.class);
    }

    private static void executeCallback(SimpleCallbackDescriptor callbackDescriptor)
    {
        if (callbackDescriptor != null)
        {
            callbackDescriptor.execute();
        }
    }
}