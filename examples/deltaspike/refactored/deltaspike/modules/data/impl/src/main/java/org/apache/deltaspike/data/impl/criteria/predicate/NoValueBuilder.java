package org.apache.deltaspike.data.impl.criteria.predicate;

import javax.persistence.metamodel.SingularAttribute;

abstract class NoValueBuilder<E, V> implements PredicateBuilder<E>
{
    private final SingularAttribute<? super E, V> att;

    NoValueBuilder(SingularAttribute<? super E, V> att)
    {
        this.att = att;
    }

    SingularAttribute<? super E, V> getAtt()
    {
        return att;
    }
}