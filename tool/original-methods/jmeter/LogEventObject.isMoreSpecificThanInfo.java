public boolean isMoreSpecificThanInfo() {
    if (level != null) {
        return level.isMoreSpecificThan(Level.INFO);
    }
    return false;
}