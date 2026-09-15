package org.apache.deltaspike.data.impl.graph;

public class EntityGraphException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public EntityGraphException(String message) {
        super(message);
    }

    public EntityGraphException(String message, Throwable cause) {
        super(message, cause);
    }

}