public UserSearchResult(SearchResult<User> copy) {
    this.objectName = copy.getObjectName();
    this.records = copy.getRecords();
    this.result = copy.getResult().stream().map(UserDTO::new).collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
    this.errorKey = copy.getErrorKey();
}