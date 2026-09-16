package org.apache.deltaspike.jsf.impl.injection;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;

public class ConverterWrapper extends AbstractContextualReferenceWrapper<Converter> implements Converter
{
    public ConverterWrapper()
    {
    }

    public ConverterWrapper(Converter wrapped, boolean fullStateSavingFallbackEnabled)
    {
        super(wrapped, fullStateSavingFallbackEnabled);
    }

    @Override
    public Object getAsObject(FacesContext facesContext, UIComponent component, String value) throws ConverterException
    {
        return getWrapped().getAsObject(facesContext, component, value);
    }

    @Override
    public String getAsString(FacesContext facesContext, UIComponent component, Object value) throws ConverterException
    {
        return getWrapped().getAsString(facesContext, component, value);
    }

    @Override
    protected Converter resolveInstanceForClass(FacesContext facesContext, Class<?> wrappedClass)
    {
        FacesConverter facesConverter = getFacesConverterAnnotation(wrappedClass);
        if (facesConverter == null)
        {
            return null;
        }

        String converterId = getConverterId(facesConverter);
        if (converterId != null)
        {
            return createConverterById(facesContext, converterId);
        }

        return createConverterByClass(facesContext, facesConverter.forClass());
    }

    private FacesConverter getFacesConverterAnnotation(Class<?> wrappedClass)
    {
        return wrappedClass.getAnnotation(FacesConverter.class);
    }

    private String getConverterId(FacesConverter facesConverter)
    {
        String value = facesConverter.value();
        return "".equals(value) ? null : value;
    }

    private Converter createConverterById(FacesContext facesContext, String converterId)
    {
        return facesContext.getApplication().createConverter(converterId);
    }

    private Converter createConverterByClass(FacesContext facesContext, Class<?> converterClass)
    {
        return facesContext.getApplication().createConverter(converterClass);
    }
}