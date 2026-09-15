package org.apache.deltaspike.jsf.impl.component.window;

import javax.faces.component.FacesComponent;
import javax.faces.component.UIOutput;

/**
 * {@link WindowIdHtmlRenderer} will render a small script needed for ajax-requests
 */
@FacesComponent(WindowIdComponent.COMPONENT_TYPE)
public class WindowIdComponent extends UIOutput
{
    public static final String COMPONENT_TYPE = "org.apache.deltaspike.WindowIdHolder";
}