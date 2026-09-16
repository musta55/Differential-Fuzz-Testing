@Override
public void afterPhase(PhaseEvent phaseEvent) {
    if (activated) {
        resolveBroadcaster().broadcastAfterEvent(phaseEvent);
    }
}