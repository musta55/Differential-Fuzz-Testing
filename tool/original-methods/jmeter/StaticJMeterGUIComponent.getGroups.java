private static List<String> getGroups(Class<?> c, TestElementMetadata metadata) {
    String[] groups = metadata.actionGroups();
    if (groups.length == 1 && groups[0].equals("")) {
        // Annotations can't hold null values, so we use empty string instead
        return null;
    }
    if (groups.length != 0) {
        return Collections.unmodifiableList(Arrays.asList(groups));
    }
    String group;
    if (Assertion.class.isAssignableFrom(c) || AbstractAssertionGui.class.isAssignableFrom(c)) {
        group = MenuFactory.ASSERTIONS;
    } else if (ConfigElement.class.isAssignableFrom(c) || AbstractConfigGui.class.isAssignableFrom(c)) {
        group = MenuFactory.CONFIG_ELEMENTS;
    } else if (Controller.class.isAssignableFrom(c) || AbstractControllerGui.class.isAssignableFrom(c)) {
        group = MenuFactory.CONTROLLERS;
    } else if (Visualizer.class.isAssignableFrom(c) || AbstractListenerGui.class.isAssignableFrom(c)) {
        group = MenuFactory.LISTENERS;
    } else if (PostProcessor.class.isAssignableFrom(c) || AbstractPostProcessorGui.class.isAssignableFrom(c)) {
        group = MenuFactory.POST_PROCESSORS;
    } else if (PreProcessor.class.isAssignableFrom(c) || AbstractPreProcessorGui.class.isAssignableFrom(c)) {
        group = MenuFactory.PRE_PROCESSORS;
    } else if (Sampler.class.isAssignableFrom(c) || AbstractSamplerGui.class.isAssignableFrom(c)) {
        group = MenuFactory.SAMPLERS;
    } else if (Timer.class.isAssignableFrom(c) || AbstractTimerGui.class.isAssignableFrom(c)) {
        group = MenuFactory.TIMERS;
    } else if (ThreadGroup.class.isAssignableFrom(c) || AbstractThreadGroupGui.class.isAssignableFrom(c)) {
        group = MenuFactory.THREADS;
    } else {
        throw new IllegalArgumentException("Unknown group for class " + c);
    }
    return Collections.singletonList(group);
}