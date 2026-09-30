public String[] getTimeBuckets() {
    if (dimensionsScheme == null) {
        return null;
    }
    return dimensionsScheme.getTimeBuckets();
}