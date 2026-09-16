package org.apache.deltaspike.data.impl.criteria.predicate;

import java.util.Collections;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.metamodel.SingularAttribute;

public class IsNotNull<E, V> extends NoValueBuilder<E, V>
{
    public IsNotNull(SingularAttribute<? super E, V> att)
    {
        super(att);
    }

    @Override
    public List<Predicate> build(CriteriaBuilder builder, Path<E> path)
    {
        return Collections.singletonList(builder.isNotNull(path.get(getAtt())));
    }
}