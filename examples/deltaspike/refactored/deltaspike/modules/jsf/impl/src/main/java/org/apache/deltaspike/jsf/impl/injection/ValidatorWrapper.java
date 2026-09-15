package org.apache.deltaspike.jsf.impl.injection;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

public class ValidatorWrapper extends AbstractContextualReferenceWrapper<Validator> implements Validator
{
    public ValidatorWrapper()
    {
    }

    public ValidatorWrapper(Validator wrapped, boolean fullStateSavingFallbackEnabled)
    {
        super(wrapped, fullStateSavingFallbackEnabled);
    }

    @Override
    public void validate(FacesContext facesContext, UIComponent component, Object value) throws ValidatorException
    {
        getWrapped().validate(facesContext, component, value);
    }

    @Override
    protected Validator resolveInstanceForClass(FacesContext facesContext, Class<?> wrappedClass)
    {
        if (!hasFacesValidatorAnnotation(wrappedClass))
        {
            return null;
        }

        return facesContext.getApplication().createValidator(getValidatorId(wrappedClass));
    }

    private boolean hasFacesValidatorAnnotation(Class<?> wrappedClass)
    {
        return wrappedClass.getAnnotation(FacesValidator.class) != null;
    }

    private String getValidatorId(Class<?> wrappedClass)
    {
        return wrappedClass.getAnnotation(FacesValidator.class).value();
    }
}