/**
 * @param searcher
 * @param node
 */
private static boolean searchInNode(Searcher searcher, SearchableTreeNode node) {
    node.reset();
    Object userObject = node.getUserObject();
    try {
        if (!(userObject instanceof Searchable)) {
            return false;
        }
        Searchable searchable = (Searchable) userObject;
        boolean foundInCurrentNode = searcher.search(searchable.getSearchableTokens());
        node.setNodeHasMatched(foundInCurrentNode);
        boolean foundInChildren = false;
        for (int i = 0; i < node.getChildCount(); i++) {
            foundInChildren |= searchInNode(searcher, (SearchableTreeNode) node.getChildAt(i));
        }
        if (!node.isNodeHasMatched()) {
            node.setChildrenNodesHaveMatched(foundInChildren);
        }
        node.updateState();
        return node.isNodeHasMatched() || node.isChildrenNodesHaveMatched();
    } catch (Exception e) {
        log.error("Error extracting data from tree node using searcher:{}", searcher, e);
        return false;
    }
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

