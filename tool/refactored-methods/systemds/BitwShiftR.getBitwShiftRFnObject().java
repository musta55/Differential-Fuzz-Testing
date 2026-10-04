public static BitwShiftR getBitwShiftRFnObject() {
    if (singleObj == null) {
        synchronized (BitwShiftR.class) {
            if (singleObj == null) {
                singleObj = new BitwShiftR();
            }
        }
    }
    return singleObj;
}