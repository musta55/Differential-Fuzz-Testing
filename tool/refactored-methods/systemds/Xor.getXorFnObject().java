public static Xor getXorFnObject() {
    if (singleObj == null) {
        synchronized (Xor.class) {
            if (singleObj == null) {
                singleObj = new Xor();
            }
        }
    }
    return singleObj;
}