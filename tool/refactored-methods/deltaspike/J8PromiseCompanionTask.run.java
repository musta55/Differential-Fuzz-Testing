public void run() {
    try {
        COMPLETABLE_FUTURE_COMPLETE.invoke(dep, fn.call());
    } catch (Exception e) {
        Throwable cause = e instanceof InvocationTargetException ? e.getCause() : e;
        try {
            COMPLETABLE_FUTURE_COMPLETE_ERROR.invoke(dep, cause);
        } catch (IllegalAccessException | InvocationTargetException e1) {
            throw ExceptionUtils.throwAsRuntimeException(e1.getCause());
        }
    }
}