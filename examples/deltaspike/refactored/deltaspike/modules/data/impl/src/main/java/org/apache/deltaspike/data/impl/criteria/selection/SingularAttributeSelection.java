package org.apache.deltaspike.data.impl.criteria.selection;

import javax.persistence.metamodel.SingularAttribute;

import org.apache.deltaspike.data.api.criteria.QuerySelection;

public abstract class SingularAttributeSelection<P, X> implements QuerySelection<P, X>
{
    protected final SingularAttribute<? super P, X> attribute;

    public SingularAttributeSelection(SingularAttribute<? super P, X> attribute)
    {
        this.attribute = attribute;
    }

    public SingularAttribute<? super P, X> getAttribute()
    {
        return attribute;
    }
}