@Override
public void beforePhase(PhaseEvent phaseEvent) {
    if (activated) {
        resolveBroadcaster().broadcastBeforeEvent(phaseEvent);
    }
}