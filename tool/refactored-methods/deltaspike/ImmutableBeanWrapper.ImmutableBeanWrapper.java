/**
 * Instantiate a new {@link ImmutableBeanWrapper}.
 *
 * @param bean        the bean to wrapped the lifecycle to
 * @param name        the name of the bean
 * @param qualifiers  the qualifiers of the bean
 * @param scope       the scope of the bean
 * @param stereotypes the bean's stereotypes
 * @param types       the types of the bean
 * @param alternative whether the bean is an alternative
 * @param nullable    true if the bean is nullable
 * @param toString    the string which should be returned by #{@link #toString()}
 */
public ImmutableBeanWrapper(Bean<T> bean, String name, Set<Annotation> qualifiers, Class<? extends Annotation> scope, Set<Class<? extends Annotation>> stereotypes, Set<Type> types, boolean alternative, boolean nullable, String toString) {
    super(bean.getBeanClass(), name, qualifiers, scope, stereotypes, types, alternative, nullable, bean.getInjectionPoints(), toString);
    this.wrapped = bean;
}