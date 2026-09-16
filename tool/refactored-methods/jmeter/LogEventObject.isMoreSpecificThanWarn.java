public boolean isMoreSpecificThanWarn() {
    return logLevel != null && logLevel.isMoreSpecificThan(Level.WARN);
}