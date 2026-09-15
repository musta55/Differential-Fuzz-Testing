package org.apache.deltaspike.jpa.impl.transaction;

import javax.annotation.Resource;
import javax.enterprise.context.Dependent;
import javax.transaction.UserTransaction;

//we need to keep it separated,
//because the injection of UserTransaction might fail with an exception (see DELTASPIKE-986)
@Dependent
public class ManagedUserTransactionResolver
{
    @Resource
    private UserTransaction userTransaction;

    public UserTransaction resolveUserTransaction()
    {
        return userTransaction;
    }
}