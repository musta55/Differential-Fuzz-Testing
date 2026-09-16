package org.apache.deltaspike.data.impl.criteria.selection.numeric;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Selection;
import javax.persistence.metamodel.SingularAttribute;

import org.apache.deltaspike.data.api.criteria.QuerySelection;

public class CountDistinct<P> implements QuerySelection<P, Long>
{
    private final SingularAttribute<? super P, ?> attribute;

    public CountDistinct(SingularAttribute<? super P, ?> attribute)
    {
        this.attribute = attribute;
    }

    @Override
    public <R> Selection<Long> toSelection(CriteriaQuery<R> query, CriteriaBuilder builder, Path<? extends P> path)
    {
        return builder.countDistinct(path.get(attribute));
    }
}