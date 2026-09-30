public String[] getTimeBuckets() {
    if (dimensionsScheme == null) {
        return new String[0];
    }
    return dimensionsScheme.getTimeBuckets();
}