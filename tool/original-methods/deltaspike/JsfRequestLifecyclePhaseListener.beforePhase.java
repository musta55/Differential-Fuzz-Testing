@Override
public void beforePhase(PhaseEvent phaseEvent) {
    if (this.activated) {
        resolveBroadcaster().broadcastBeforeEvent(phaseEvent);
    }
}