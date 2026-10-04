public static void setContext(TaskContext context) {
    if (CTX.get() != null)
        throw new IllegalStateException();
    CTX.set(context);
}