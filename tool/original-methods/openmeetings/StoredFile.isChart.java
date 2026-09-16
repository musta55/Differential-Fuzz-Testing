public boolean isChart() {
    if (mime == null) {
        return false;
    }
    return CHART_TYPES.contains(mime);
}