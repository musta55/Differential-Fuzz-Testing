package org.apache.deltaspike.core.api.literal;

import org.apache.deltaspike.core.api.config.view.controller.ViewControllerRef;

import javax.enterprise.util.AnnotationLiteral;

/**
 * Literal for {@link org.apache.deltaspike.core.api.config.view.controller.ViewControllerRef}
 */
public class ViewControllerRefLiteral extends AnnotationLiteral<ViewControllerRef> implements ViewControllerRef
{
    private static final long serialVersionUID = 8582580975876369665L;

    private final Class<?> value;
    private final String name;

    public ViewControllerRefLiteral(Class<?> value, String name)
    {
        this.value = value;
        this.name = name;
    }

    @Override
    public Class<?> value()
    {
        return this.value;
    }

    @Override
    public String name()
    {
        return this.name;
    }
}