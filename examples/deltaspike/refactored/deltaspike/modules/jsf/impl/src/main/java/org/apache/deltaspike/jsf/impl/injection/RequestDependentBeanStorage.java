package org.apache.deltaspike.jsf.impl.injection;

import javax.enterprise.context.RequestScoped;

//TODO merge with AbstractBeanStorage if ViewDependentBeanStorage isn't needed (see MYFACES-3805)
@RequestScoped
public class RequestDependentBeanStorage extends AbstractBeanStorage
{
}