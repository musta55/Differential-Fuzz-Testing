package org.apache.deltaspike.data.impl.audit;

public interface PrePersistAuditListener {

    void prePersist(Object entity);

}