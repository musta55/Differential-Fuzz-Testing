public static RevIndex getRevIndexFnObject() {
    if (singleObj == null) {
        synchronized (RevIndex.class) {
            if (singleObj == null) {
                singleObj = new RevIndex();
            }
        }
    }
    return singleObj;
}