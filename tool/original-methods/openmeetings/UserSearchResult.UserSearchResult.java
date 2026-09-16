public UserSearchResult(SearchResult<User> copy) {
    this.objectName = copy.getObjectName();
    this.records = copy.getRecords();
    this.result = new ArrayList<>(copy.getResult().size());
    for (User u : copy.getResult()) {
        result.add(new UserDTO(u));
    }
    this.errorKey = copy.getErrorKey();
}