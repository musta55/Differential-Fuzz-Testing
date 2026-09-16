public boolean isDelete() {
    String prefix = this.getPrefix();
    return prefix.equalsIgnoreCase(DEFAULT_DELETE_PREFIX) || prefix.equalsIgnoreCase(DEFAULT_REMOVE_PREFIX);
}