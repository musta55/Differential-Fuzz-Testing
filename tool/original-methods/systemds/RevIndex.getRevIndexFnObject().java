public static RevIndex getRevIndexFnObject() {
    if (singleObj == null)
        singleObj = new RevIndex();
    return singleObj;
}