@Override
protected ArrayList<Method> getDelegateMethods(Class<?> targetClass, ArrayList<Method> allMethods) {
    if (!StateHolder.class.isAssignableFrom(targetClass)) {
        ArrayList<Method> delegateMethods = new ArrayList<Method>();
        delegateMethods.addAll(Arrays.asList(StateHolder.class.getDeclaredMethods()));
        delegateMethods.addAll(Arrays.asList(PartialStateHolder.class.getDeclaredMethods()));
        return delegateMethods;
    }
    if (!PartialStateHolder.class.isAssignableFrom(targetClass)) {
        ArrayList<Method> delegateMethods = new ArrayList<Method>();
        delegateMethods.addAll(Arrays.asList(PartialStateHolder.class.getDeclaredMethods()));
        return delegateMethods;
    }
    return null;
}