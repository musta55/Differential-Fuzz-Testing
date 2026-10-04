public static Xor getXorFnObject() {
    if (singleObj == null)
        singleObj = new Xor();
    return singleObj;
}