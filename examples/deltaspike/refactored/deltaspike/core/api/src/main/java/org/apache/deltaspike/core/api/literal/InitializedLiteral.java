package org.apache.deltaspike.core.api.literal;

import javax.enterprise.util.AnnotationLiteral;

import org.apache.deltaspike.core.api.lifecycle.Initialized;

/**
 * Annotation literal for {@link Initialized}.
 */
public class InitializedLiteral extends AnnotationLiteral<Initialized> implements Initialized
{
    public static final Initialized INSTANCE = new InitializedLiteral();

    private static final long serialVersionUID = 2392444150652655120L;
}