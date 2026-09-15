package org.apache.deltaspike.cdise.api;

import java.lang.annotation.Annotation;

/**
 * Control native CDI Container Contexts.
 * Just inject this interface and you gain manual access over built-in Contexts.
 * The respective integration code will provide a &064;Dependent scoped instance
 * which itself is stateless.
 *
 * The {@link #startContext(Class)} and {@link #stopContext(Class)} only affect
 * the current Thread. When leaving a Thread each started context needs to get
 * stopped as well (best practice is to do that in a <i>finally</i> block.
 *
 * If a container supports controlling the Session Context then each Thread will
 * get a new 'dummy' storage assigned. It is not intended to 'attach' to a real
 * Session but to allow the re-use of existing beans.
 *
 * Many containers make heavy use of ThreadLocals. Thus it might be necessary to
 * call
 * <pre>
 *     contextControl.startContext(ApplicationScoped.class);
 * </pre>
 * to 'attach' or 'activate' the ApplicationContext within your current Thread.
 */
public interface ContextControl
{
    /**
     * This will start all container built-in Contexts
     */
    void startContexts();

    /**
     * Stop all container built-in Contexts and destroy all beans properly
     */
    void stopContexts();

    /**
     * Start the specified scope. This only works for scopes which are handled
     * by the CDI container itself. Custom scoped of 3rd party
     * Context implementations shall be started directly (they are portable anyway).
     *
     * @param scopeClass e.g. RequestScoped.class
     */
    void startContext(Class<? extends Annotation> scopeClass);

    /**
     * Stop the specified scope. This only works for scopes which are handled
     * by the CDI container itself. Custom scoped of 3rd party
     * Context implementations shall be stopped directly (they are portable anyway).
     *
     * @param scopeClass e.g. RequestScoped.class
     */
    void stopContext(Class<? extends Annotation> scopeClass);
}