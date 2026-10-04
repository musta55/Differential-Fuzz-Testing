public boolean isMoreSpecificThanInfo() {
    return logLevel != null && logLevel.isMoreSpecificThan(Level.INFO);
}