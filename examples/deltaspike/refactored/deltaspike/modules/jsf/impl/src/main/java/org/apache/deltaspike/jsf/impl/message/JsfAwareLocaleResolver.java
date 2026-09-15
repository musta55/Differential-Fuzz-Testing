package org.apache.deltaspike.jsf.impl.message;

import org.apache.deltaspike.core.impl.message.DefaultLocaleResolver;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Specializes;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import java.util.Iterator;
import java.util.Locale;

@ApplicationScoped
@Specializes
public class JsfAwareLocaleResolver extends DefaultLocaleResolver
{
    private static final long serialVersionUID = -8776583393262804931L;

    @Override
    public Locale getLocale()
    {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        if (facesContext == null || facesContext.getCurrentPhaseId() == null)
        {
            return super.getLocale();
        }

        UIViewRoot viewRoot = facesContext.getViewRoot();
        if (viewRoot != null)
        {
            Locale result = viewRoot.getLocale();
            if (isSupportedLocale(facesContext, result))
            {
                return result;
            }
        }

        Locale defaultLocale = facesContext.getApplication().getDefaultLocale();
        return defaultLocale != null ? defaultLocale : super.getLocale();
    }

    private boolean isSupportedLocale(FacesContext facesContext, Locale locale)
    {
        if (locale == null)
        {
            return false;
        }

        Iterator<Locale> supportedLocales = facesContext.getApplication().getSupportedLocales();
        while (supportedLocales.hasNext())
        {
            if (locale.equals(supportedLocales.next()))
            {
                return true;
            }
        }

        return false;
    }
}