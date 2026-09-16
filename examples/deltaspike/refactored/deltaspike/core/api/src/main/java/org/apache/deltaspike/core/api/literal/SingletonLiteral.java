package org.apache.deltaspike.core.api.literal;

import javax.enterprise.util.AnnotationLiteral;
import javax.inject.Singleton;

/**
 * Literal for {@link javax.inject.Singleton}
 */
public class SingletonLiteral extends AnnotationLiteral<Singleton> implements Singleton
{
    private static final long serialVersionUID = 2387772903537960411L;
}