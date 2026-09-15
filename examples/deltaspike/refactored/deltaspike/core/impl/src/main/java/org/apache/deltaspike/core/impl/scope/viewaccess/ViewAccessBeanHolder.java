package org.apache.deltaspike.core.impl.scope.viewaccess;

import org.apache.deltaspike.core.api.scope.WindowScoped;
import org.apache.deltaspike.core.impl.scope.AbstractBeanHolder;

@WindowScoped
public class ViewAccessBeanHolder extends AbstractBeanHolder<String>
{
    private static final long serialVersionUID = 6313403410718143908L;
}