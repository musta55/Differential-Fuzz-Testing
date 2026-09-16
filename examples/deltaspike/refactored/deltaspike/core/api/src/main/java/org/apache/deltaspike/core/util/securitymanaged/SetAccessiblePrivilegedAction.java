package org.apache.deltaspike.core.util.securitymanaged;

import javax.enterprise.inject.Typed;
import java.lang.reflect.AccessibleObject;
import java.security.PrivilegedAction;

/**
 * PrivilegedAction instance to enabling access to the specified {@link AccessibleObject}.
 * It's only useful if {@link System#getSecurityManager()} returns a {@link SecurityManager}.
 */
@Typed()
public class SetAccessiblePrivilegedAction implements PrivilegedAction<Void>
{
    private final AccessibleObject member;

    public SetAccessiblePrivilegedAction(AccessibleObject member)
    {
        this.member = member;
    }

    @Override
    public Void run()
    {
        member.setAccessible(true);
        return null;
    }
}