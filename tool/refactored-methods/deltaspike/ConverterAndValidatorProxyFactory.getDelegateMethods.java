@Override
protected ArrayList<Method> getDelegateMethods(Class<?> targetClass, ArrayList<Method> allMethods) {
    ArrayList<Method> delegateMethods = new ArrayList<>();
    if (!StateHolder.class.isAssignableFrom(targetClass)) {
        delegateMethods.addAll(Arrays.asList(StateHolder.class.getDeclaredMethods()));
    }
    if (!PartialStateHolder.class.isAssignableFrom(targetClass)) {
        delegateMethods.addAll(Arrays.asList(PartialStateHolder.class.getDeclaredMethods()));
    }
    return delegateMethods.isEmpty() ? null : delegateMethods;
}