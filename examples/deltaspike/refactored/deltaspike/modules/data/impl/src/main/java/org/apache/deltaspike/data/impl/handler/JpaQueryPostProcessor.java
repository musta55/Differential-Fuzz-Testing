package org.apache.deltaspike.data.impl.handler;

import javax.persistence.Query;

public interface JpaQueryPostProcessor
{
    Query postProcess(CdiQueryInvocationContext context, Query query);
}