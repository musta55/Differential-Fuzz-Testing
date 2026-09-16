private static List<String> getGroups(Class<?> c, TestElementMetadata metadata) {
    String[] groups = metadata.actionGroups();
    if (groups.length == 1 && groups[0].equals("")) {
        return null;
    }
    if (groups.length != 0) {
        return Collections.unmodifiableList(Arrays.asList(groups));
    }
    return Collections.singletonList(determineGroup(c));
}
// ---- helper method(s) introduced by the refactoring ----
private ResourceBundle loadResourceBundle(Class<?> c, TestElementMetadata metadata) {
    String resourceBundleName = metadata.resourceBundle();
    if (!resourceBundleName.isEmpty()) {
        return ResourceBundle.getBundle(c.getName() + "Resources");
    } else if (labelResource.equals("displayName")) {
        return ResourceBundle.getBundle(c.getName() + "Resources");
    } else {
        return null;
    }
}

private static String determineGroup(Class<?> c) {
    if (isAssertion(c)) {
        return MenuFactory.ASSERTIONS;
    } else if (isConfigElement(c)) {
        return MenuFactory.CONFIG_ELEMENTS;
    } else if (isController(c)) {
        return MenuFactory.CONTROLLERS;
    } else if (isVisualizer(c)) {
        return MenuFactory.LISTENERS;
    } else if (isPostProcessor(c)) {
        return MenuFactory.POST_PROCESSORS;
    } else if (isPreProcessor(c)) {
        return MenuFactory.PRE_PROCESSORS;
    } else if (isSampler(c)) {
        return MenuFactory.SAMPLERS;
    } else if (isTimer(c)) {
        return MenuFactory.TIMERS;
    } else if (isThreadGroup(c)) {
        return MenuFactory.THREADS;
    } else {
        throw new IllegalArgumentException("Unknown group for class " + c);
    }
}

private static boolean isAssertion(Class<?> c) {
    return Assertion.class.isAssignableFrom(c) || AbstractAssertionGui.class.isAssignableFrom(c);
}

private static boolean isConfigElement(Class<?> c) {
    return ConfigElement.class.isAssignableFrom(c) || AbstractConfigGui.class.isAssignableFrom(c);
}

private static boolean isController(Class<?> c) {
    return Controller.class.isAssignableFrom(c) || AbstractControllerGui.class.isAssignableFrom(c);
}

private static boolean isVisualizer(Class<?> c) {
    return Visualizer.class.isAssignableFrom(c) || AbstractListenerGui.class.isAssignableFrom(c);
}

private static boolean isPostProcessor(Class<?> c) {
    return PostProcessor.class.isAssignableFrom(c) || AbstractPostProcessorGui.class.isAssignableFrom(c);
}

private static boolean isPreProcessor(Class<?> c) {
    return PreProcessor.class.isAssignableFrom(c) || AbstractPreProcessorGui.class.isAssignableFrom(c);
}

private static boolean isSampler(Class<?> c) {
    return Sampler.class.isAssignableFrom(c) || AbstractSamplerGui.class.isAssignableFrom(c);
}

private static boolean isTimer(Class<?> c) {
    return Timer.class.isAssignableFrom(c) || AbstractTimerGui.class.isAssignableFrom(c);
}

private static boolean isThreadGroup(Class<?> c) {
    return ThreadGroup.class.isAssignableFrom(c) || AbstractThreadGroupGui.class.isAssignableFrom(c);
}

