/**
 * Checks if the context for the given scope annotation is active.
 *
 * @param scopeAnnotationClass The scope annotation (e.g. @RequestScoped.class)
 * @param beanManager The {@link BeanManager}
 * @return If the context is active.
 */
public static boolean isContextActive(Class<? extends Annotation> scopeAnnotationClass, BeanManager beanManager) {
    try {
        if (beanManager.getContext(scopeAnnotationClass) == null || !beanManager.getContext(scopeAnnotationClass).isActive()) {
            return false;
        }
    } catch (ContextNotActiveException e) {
        return false;
    }
    return true;
}