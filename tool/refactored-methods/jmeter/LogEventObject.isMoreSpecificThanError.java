public boolean isMoreSpecificThanError() {
    return logLevel != null && logLevel.isMoreSpecificThan(Level.ERROR);
}