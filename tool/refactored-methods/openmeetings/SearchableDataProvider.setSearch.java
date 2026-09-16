public void setSearch(String search) {
    this.search = isValidSearch(search) ? search.trim() : null;
}
// ---- helper method(s) introduced by the refactoring ----
private boolean isValidSearch(String search) {
    return search != null && !search.trim().isEmpty();
}

