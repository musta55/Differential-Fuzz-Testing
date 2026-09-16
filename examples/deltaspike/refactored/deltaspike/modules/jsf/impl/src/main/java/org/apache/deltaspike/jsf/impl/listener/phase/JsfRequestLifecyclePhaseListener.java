package org.apache.deltaspike.jsf.impl.listener.phase;

import org.apache.deltaspike.core.api.provider.BeanProvider;
import org.apache.deltaspike.core.spi.activation.Deactivatable;
import org.apache.deltaspike.core.util.ClassDeactivationUtils;

import javax.enterprise.inject.Typed;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;

/**
 * PhaseListener for triggering {@link JsfRequestLifecycleBroadcaster}
 */

@Typed(Deactivatable.class) //don't use PhaseListener - the broadcaster would fire to this listener as well
public class JsfRequestLifecyclePhaseListener implements PhaseListener, Deactivatable
{
    private static final long serialVersionUID = -3351903831660165998L;

    private final boolean activated;

    public JsfRequestLifecyclePhaseListener()
    {
        this.activated = ClassDeactivationUtils.isActivated(getClass());
    }

    @Override
    public void beforePhase(PhaseEvent phaseEvent)
    {
        if (activated)
        {
            resolveBroadcaster().broadcastBeforeEvent(phaseEvent);
        }
    }

    @Override
    public void afterPhase(PhaseEvent phaseEvent)
    {
        if (activated)
        {
            resolveBroadcaster().broadcastAfterEvent(phaseEvent);
        }
    }

    @Override
    public PhaseId getPhaseId()
    {
        return PhaseId.ANY_PHASE;
    }

    private JsfRequestLifecycleBroadcaster resolveBroadcaster()
    {
        //cdi has to inject the events,...
        return BeanProvider.getContextualReference(JsfRequestLifecycleBroadcaster.class);
    }
}