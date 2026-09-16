package org.apache.deltaspike.data.impl.criteria.predicate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;

import org.apache.deltaspike.data.api.criteria.Criteria;

public class OrBuilder<P> implements PredicateBuilder<P>
{

    private final Criteria<P, P>[] criteria;

    public OrBuilder(Criteria<P, P>... criteria)
    {
        this.criteria = criteria;
    }

    @Override
    public List<Predicate> build(CriteriaBuilder builder, Path<P> path)
    {
        List<Predicate> orPredicates = new ArrayList<>(criteria.length);
        for (Criteria<P, P> c : criteria)
        {
            orPredicates.add(builder.and(c.predicates(builder, path).toArray(new Predicate[0])));
        }
        return Arrays.asList(builder.or(orPredicates.toArray(new Predicate[0])));
    }

}