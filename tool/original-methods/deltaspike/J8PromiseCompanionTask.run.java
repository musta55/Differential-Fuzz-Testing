public void run() {
    try {
        COMPLETABLE_FUTURE_COMPLETE.invoke(dep, fn.call());
    } catch (final InvocationTargetException e) {
        try {
            COMPLETABLE_FUTURE_COMPLETE_ERROR.invoke(dep, e.getCause());
        } catch (IllegalAccessException e1) {
            throw ExceptionUtils.throwAsRuntimeException(e1);
        } catch (final InvocationTargetException e1) {
            throw ExceptionUtils.throwAsRuntimeException(e1.getCause());
        }
    } catch (Exception e) {
        try {
            COMPLETABLE_FUTURE_COMPLETE_ERROR.invoke(dep, e);
        } catch (IllegalAccessException e1) {
            throw ExceptionUtils.throwAsRuntimeException(e1);
        } catch (final InvocationTargetException e1) {
            throw ExceptionUtils.throwAsRuntimeException(e1.getCause());
        }
    }
}