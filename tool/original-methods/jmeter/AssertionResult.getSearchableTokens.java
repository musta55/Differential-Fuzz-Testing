@Override
public List<String> getSearchableTokens() throws Exception {
    List<String> datasToSearch = new ArrayList<>(2);
    datasToSearch.add(getName());
    datasToSearch.add(getFailureMessage());
    return datasToSearch;
}