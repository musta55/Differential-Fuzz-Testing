@Override
public void afterPhase(PhaseEvent phaseEvent) {
    if (this.activated) {
        resolveBroadcaster().broadcastAfterEvent(phaseEvent);
    }
}