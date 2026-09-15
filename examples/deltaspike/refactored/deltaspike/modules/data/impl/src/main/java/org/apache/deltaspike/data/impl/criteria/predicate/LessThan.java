package org.apache.deltaspike.data.impl.criteria.predicate;

import java.util.Collections;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.metamodel.SingularAttribute;

public class LessThan<E, V extends Comparable<? super V>> extends SingleValueBuilder<E, V>
{
    public LessThan(SingularAttribute<? super E, V> att, V value)
    {
        super(att, value);
    }

    @Override
    public List<Predicate> build(CriteriaBuilder builder, Path<E> path)
    {
        return Collections.singletonList(builder.lessThan(path.get(getAtt()), getValue()));
    }
}