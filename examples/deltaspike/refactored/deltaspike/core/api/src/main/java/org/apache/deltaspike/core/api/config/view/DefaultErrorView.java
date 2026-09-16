package org.apache.deltaspike.core.api.config.view;

/**
 * Abstract class which marks an error view.
 *
 * It's an abstract class instead of an interface, because it can be used for navigation (which is restricted to
 * classes).
 */
public abstract class DefaultErrorView implements ViewConfig
{
}