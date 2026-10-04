private Object createDefaultValue(int i) {
    // CHECKSTYLE IGNORE ReturnCount
    Class<?> colClass = getColumnClass(i);
    try {
        return colClass.getDeclaredConstructor().newInstance();
    } catch (Exception e) {
        for (Object initArg : DEFAULT_ARGS) {
            try {
                Constructor<?> constr = colClass.getConstructor(initArg.getClass());
                return constr.newInstance(initArg);
            } catch (ReflectiveOperationException ignored) {
                // no need to log this, as we are just trying out all available default args
            }
        }
    }
    return "";
}