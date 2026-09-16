/**
 * @see org.apache.jmeter.engine.TreeCloner#addNodeToTree(java.lang.Object)
 */
@Override
protected Object addNodeToTree(Object node) {
    if ((VALIDATION_IGNORE_TIMERS && node instanceof Timer) || (VALIDATION_IGNORE_BACKENDS && node instanceof Backend)) {
        // don't add timer or backend
        return node;
    } else {
        Object clonedNode = super.addNodeToTree(node);
        if (clonedNode instanceof ThreadGroup) {
            configureThreadGroup((ThreadGroup) clonedNode);
        } else if (clonedNode instanceof OpenModelThreadGroup) {
            configureOpenModelThreadGroup((OpenModelThreadGroup) clonedNode);
        }
        return clonedNode;
    }
}
// ---- helper method(s) introduced by the refactoring ----
private static void configureThreadGroup(ThreadGroup tg) {
    tg.setNumThreads(VALIDATION_NUMBER_OF_THREADS);
    tg.setScheduler(false);
    tg.setProperty(ThreadGroup.DELAY, 0);
    if (tg.getSamplerController() instanceof LoopController) {
        ((LoopController) tg.getSamplerController()).setLoops(VALIDATION_ITERATIONS);
    }
}

private static void configureOpenModelThreadGroup(OpenModelThreadGroup tg) {
    tg.setRandomSeedString("0");
    // Launch all the iterations during the first second, and leave one hour for the threads to complete
    tg.setScheduleString("rate(" + VALIDATION_ITERATIONS + " / sec) even_arrivals(1 sec) pause(1 hour)");
}

