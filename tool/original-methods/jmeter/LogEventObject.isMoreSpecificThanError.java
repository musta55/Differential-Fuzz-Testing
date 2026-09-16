public boolean isMoreSpecificThanError() {
    if (level != null) {
        return level.isMoreSpecificThan(Level.ERROR);
    }
    return false;
}