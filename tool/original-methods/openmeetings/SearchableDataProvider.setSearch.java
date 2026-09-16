public void setSearch(String search) {
    if (search != null && !search.trim().isEmpty()) {
        this.search = search.trim();
    } else {
        this.search = null;
    }
}