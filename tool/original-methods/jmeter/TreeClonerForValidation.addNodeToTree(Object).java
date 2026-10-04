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
        if (clonedNode instanceof org.apache.jmeter.threads.ThreadGroup) {
            ThreadGroup tg = (ThreadGroup) clonedNode;
            tg.setNumThreads(VALIDATION_NUMBER_OF_THREADS);
            tg.setScheduler(false);
            tg.setProperty(ThreadGroup.DELAY, 0);
            if (((AbstractThreadGroup) clonedNode).getSamplerController() instanceof LoopController) {
                ((LoopController) ((AbstractThreadGroup) clonedNode).getSamplerController()).setLoops(VALIDATION_ITERATIONS);
            }
        } else if (clonedNode instanceof OpenModelThreadGroup) {
            OpenModelThreadGroup tg = (OpenModelThreadGroup) clonedNode;
            tg.setRandomSeedString("0");
            // Launch all the iterations during the first second, and leave one hour for the threads to complete
            tg.setScheduleString("rate(" + VALIDATION_ITERATIONS + " / sec) even_arrivals(1 sec) pause(1 hour)");
        }
        return clonedNode;
    }
}