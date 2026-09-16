package org.apache.deltaspike.jpa.api.transaction;

import org.apache.deltaspike.core.api.provider.BeanProvider;

import javax.enterprise.context.ApplicationScoped;
import java.util.concurrent.Callable;

/**
 * <p></p>This class allows to execute CDI-unmanaged code blocks in a
 * &#064;Transactional manner. This is handy if you like e.g. to execute
 * database code in a unit test tearDown method.</p>
 *
 * <p><b>Attention:</b> please be aware that this helper only works for
 * &#064;Transactional with auto-detecting the EntityManager!
 * If you need to manually specify the EntityManager Qualifier
 * for another EntityManager, then you need to copy this code and adopt it.</p>
 * <p> Usage:
 * <pre>
 *  SomeEntity retVal = TransactionHelper.getInstance().executeTransactional( new Callable<Integer>() {
 *    private @Inject EntityManager em;
 *    public SomeEntity call() throws Exception {
 *      return em.find(entityId, SomeEntity.class);
 *    }
 *  } );
 * </pre>
 * </p>
 */
@ApplicationScoped
public class TransactionHelper
{
    public static TransactionHelper getInstance()
    {
        return BeanProvider.getContextualReference(TransactionHelper.class);
    }

    /**
     * Execute the given {@link Callable} in a Transitional manner.
     *
     * @param callable which will get executed in a &#064;Transactional block
     * @param <T> the return type of the executed {@link Callable}
     * @return the return value of the executed {@link Callable}
     * @throws Exception
     */
    @Transactional
    public <T> T executeTransactional(Callable<T> callable) throws Exception
    {
        return callable.call();
    }
}