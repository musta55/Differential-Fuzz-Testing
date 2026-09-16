/**
 * return true if a match occurred
 */
private boolean doSearch() {
    String wordToSearch = searchTF.getText();
    if (StringUtils.isEmpty(wordToSearch)) {
        return false;
    }
    Searcher searcher = isRegexpCB.isSelected() ? new RegexpSearcher(isCaseSensitiveCB.isSelected(), searchTF.getText()) : new RawTextSearcher(isCaseSensitiveCB.isSelected(), searchTF.getText());
    return searchInNode(searcher, (SearchableTreeNode) defaultMutableTreeNode);
}