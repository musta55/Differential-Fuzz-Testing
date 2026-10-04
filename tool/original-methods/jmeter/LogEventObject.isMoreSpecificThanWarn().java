public boolean isMoreSpecificThanWarn() {
    if (level != null) {
        return level.isMoreSpecificThan(Level.WARN);
    }
    return false;
}