package org.apache.deltaspike.data.impl.criteria.predicate;

import java.util.Collections;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.metamodel.SingularAttribute;

public class IsNull<E, V> extends NoValueBuilder<E, V>
{
    public IsNull(SingularAttribute<? super E, V> att)
    {
        super(att);
    }

    @Override
    public List<Predicate> build(CriteriaBuilder builder, Path<E> path)
    {
        return Collections.singletonList(builder.isNull(path.get(getAtt())));
    }
}