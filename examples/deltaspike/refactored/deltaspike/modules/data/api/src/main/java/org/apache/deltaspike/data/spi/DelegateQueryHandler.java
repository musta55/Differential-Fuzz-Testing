package org.apache.deltaspike.data.spi;

/**
 * A marker interface. Used for writing custom query methods:
 * <pre>
 * public interface RepositoryExtension<E> {
 *     E saveAndFlushAndRefresh(E entity);
 * }
 *
 * public class DelegateRepositoryExtension<E> implements RepositoryExtension<E>, DelegateQueryHandler {
 *    &#064;Inject
 *    private QueryInvocationContext context;
 *
 *    &#064;Override
 *    public E saveAndFlushAndRefresh(E entity) {
 *        ...
 *    }
 * }
 * </pre>
 *
 * The extension is now usable with:
 * <pre>
 * &#064;Repository
 * public interface MySimpleRepository
 *         extends RepositoryExtension<Simple>, EntityRepository<Simple, Long> {
 * }
 * </pre>
 */
public interface DelegateQueryHandler
{
}