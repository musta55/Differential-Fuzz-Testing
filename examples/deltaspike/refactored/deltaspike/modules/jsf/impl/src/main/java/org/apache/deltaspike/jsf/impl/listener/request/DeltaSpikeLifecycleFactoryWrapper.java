package org.apache.deltaspike.jsf.impl.listener.request;

import org.apache.deltaspike.core.spi.activation.Deactivatable;
import org.apache.deltaspike.core.util.ClassDeactivationUtils;
import org.apache.deltaspike.core.util.ClassUtils;
import org.apache.deltaspike.core.util.ExceptionUtils;
import org.apache.deltaspike.jsf.api.config.JsfModuleConfig;
import org.apache.deltaspike.jsf.impl.util.JsfUtils;

import javax.faces.lifecycle.Lifecycle;
import javax.faces.lifecycle.LifecycleFactory;
import java.util.Iterator;

public class DeltaSpikeLifecycleFactoryWrapper extends LifecycleFactory implements Deactivatable
{
    private final LifecycleFactory wrapped;

    private final boolean deactivated;

    private final boolean jsfVersionWithClientWindowDetected;

    /**
     * Constructor for wrapping the given {@link LifecycleFactory}
     *
     * @param wrapped lifecycle-factory which should be wrapped
     */
    public DeltaSpikeLifecycleFactoryWrapper(LifecycleFactory wrapped)
    {
        this.wrapped = wrapped;
        this.deactivated = !ClassDeactivationUtils.isActivated(getClass());
        this.jsfVersionWithClientWindowDetected = detectClientWindowSupport();
    }

    private boolean detectClientWindowSupport() {
        boolean jsfVersionWithClientWindowDetected =
            ClassUtils.tryToLoadClassForName(JsfModuleConfig.CLIENT_WINDOW_CLASS_NAME) != null;

        if (jsfVersionWithClientWindowDetected && ClassUtils.tryToLoadClassForName(
            "org.apache.deltaspike.jsf.impl.listener.request.JsfClientWindowAwareLifecycleWrapper") == null)
        {
            jsfVersionWithClientWindowDetected = false;
            JsfUtils.logWrongModuleUsage(getClass().getName());
        }
        return jsfVersionWithClientWindowDetected;
    }

    @Override
    public void addLifecycle(String s, Lifecycle lifecycle)
    {
        wrapped.addLifecycle(s, lifecycle);
    }

    @Override
    public Lifecycle getLifecycle(String s)
    {
        Lifecycle result = this.wrapped.getLifecycle(s);

        if (this.deactivated)
        {
            return result;
        }

        return createLifecycleWrapper(result);
    }

    private Lifecycle createLifecycleWrapper(Lifecycle result) {
        if (this.jsfVersionWithClientWindowDetected)
        {
            Class<? extends Lifecycle> lifecycleWrapperClass = ClassUtils.tryToLoadClassForName(
                    "org.apache.deltaspike.jsf.impl.listener.request.JsfClientWindowAwareLifecycleWrapper");
            try
            {
                return (Lifecycle) lifecycleWrapperClass.getConstructor(new Class[] { Lifecycle.class })
                        .newInstance(new DeltaSpikeLifecycleWrapper(result));
            }
            catch (Exception e)
            {
                throw ExceptionUtils.throwAsRuntimeException(e);
            }
        }
        return new DeltaSpikeLifecycleWrapper(result);
    }

    @Override
    public Iterator<String> getLifecycleIds()
    {
        return wrapped.getLifecycleIds();
    }

    @Override
    public LifecycleFactory getWrapped()
    {
        return wrapped;
    }
}