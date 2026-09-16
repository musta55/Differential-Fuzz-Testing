/**
 * return true if a match occurred
 */
private boolean doSearch() {
    String wordToSearch = searchTF.getText();
    if (StringUtils.isEmpty(wordToSearch)) {
        return false;
    }
    Searcher searcher = isRegexpCB.isSelected() ? new RegexpSearcher(isCaseSensitiveCB.isSelected(), searchTF.getText()) : new RawTextSearcher(isCaseSensitiveCB.isSelected(), searchTF.getText());
    boolean found = searchInNode(searcher, (SearchableTreeNode) defaultMutableTreeNode);
    updateSearchTextField(found);
    return found;
}
// ---- helper method(s) introduced by the refactoring ----
private void updateSearchTextField(boolean found) {
    if (found) {
        searchTF.setBackground(Color.WHITE);
        searchTF.setForeground(Color.BLACK);
    } else {
        searchTF.setBackground(Colors.LIGHT_RED);
        searchTF.setForeground(Color.WHITE);
    }
}

