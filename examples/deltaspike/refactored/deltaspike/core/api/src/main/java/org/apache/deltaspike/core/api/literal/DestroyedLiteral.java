package org.apache.deltaspike.core.api.literal;

import javax.enterprise.util.AnnotationLiteral;

import org.apache.deltaspike.core.api.lifecycle.Destroyed;

/**
 * Annotation literal for {@link Destroyed}.
 */
public class DestroyedLiteral extends AnnotationLiteral<Destroyed> implements Destroyed
{
    public static final Destroyed INSTANCE = new DestroyedLiteral();

    private static final long serialVersionUID = 8310730593030223981L;
}