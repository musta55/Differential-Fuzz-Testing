@Override
public List<String> getSearchableTokens() throws Exception {
    List<String> datasToSearch = new ArrayList<>(2);
    if (name != null) {
        datasToSearch.add(name);
    }
    if (failureMessage != null) {
        datasToSearch.add(failureMessage);
    }
    return datasToSearch;
}