package org.apache.deltaspike.jsf.impl.message;

import org.apache.deltaspike.core.api.message.MessageContext;
import org.apache.deltaspike.core.impl.message.DefaultMessageResolver;

import javax.enterprise.inject.Specializes;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import java.util.ArrayList;
import java.util.List;

@Specializes
public class JsfMessageResolver extends DefaultMessageResolver
{
    @Override
    protected List<String> getMessageSources(MessageContext messageContext)
    {
        List<String> result = new ArrayList<>(super.getMessageSources(messageContext) /*unmodifiable-list*/);

        addBundleNames(result);

        return result;
    }

    private void addBundleNames(List<String> result)
    {
        FacesContext facesContext = FacesContext.getCurrentInstance();

        if (facesContext == null || facesContext.getCurrentPhaseId() == null)
        {
            return;
        }

        String bundleName = facesContext.getApplication().getMessageBundle();

        if (bundleName != null)
        {
            result.add(bundleName);
        }
        result.add(FacesMessage.FACES_MESSAGES); //default messages from jsf
    }
}