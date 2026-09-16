public boolean isDelete() {
    return this.getPrefix().equalsIgnoreCase(DEFAULT_DELETE_PREFIX) || this.getPrefix().equalsIgnoreCase(DEFAULT_REMOVE_PREFIX);
}