/**
 * @see LoopIterationListener#iterationStart(LoopIterationEvent)
 */
@Override
public void iterationStart(LoopIterationEvent event) {
    int numIteration = getNumIterations(event);
    if (event.getIteration() == numIteration) {
        reInitialize();
    }
}
// ---- helper method(s) introduced by the refactoring ----
private static int getNumIterations(LoopIterationEvent event) {
    int numIteration = 1;
    // Bug 39509: iteration to 0 for all controllers which are not LoopController (and TG)
    if (!(event.getSource() instanceof LoopController)) {
        numIteration = 0;
    }
    return numIteration;
}

