public static void incrementFailuresCounter() {
    numFailures.increment();
    // This is very rare and am not sure it is possible at all. Our initial experiments never encountered this case.
    // Note: all the native calls have a fallback to Java; so if the user wants she can recompile SystemDS by
    // commenting this exception and everything should work fine.
    throw new RuntimeException("Unexpected ERROR: OOM caused during JNI transfer. Please disable native BLAS by setting enviroment variable: SYSTEMDS_BLAS=none");
}