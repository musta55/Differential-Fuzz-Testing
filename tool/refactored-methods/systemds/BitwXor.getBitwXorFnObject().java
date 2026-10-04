public static BitwXor getBitwXorFnObject() {
    if (singleObj == null) {
        synchronized (BitwXor.class) {
            if (singleObj == null) {
                singleObj = new BitwXor();
            }
        }
    }
    return singleObj;
}