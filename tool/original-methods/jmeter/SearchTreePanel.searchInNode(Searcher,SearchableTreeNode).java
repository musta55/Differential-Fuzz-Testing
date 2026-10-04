/**
 * @param searcher
 * @param node
 */
private static boolean searchInNode(Searcher searcher, SearchableTreeNode node) {
    node.reset();
    Object userObject = node.getUserObject();
    try {
        Searchable searchable;
        if (userObject instanceof Searchable) {
            searchable = (Searchable) userObject;
        } else {
            return false;
        }
        if (searcher.search(searchable.getSearchableTokens())) {
            node.setNodeHasMatched(true);
        }
        boolean foundInChildren = false;
        for (int i = 0; i < node.getChildCount(); i++) {
            searchInNode(searcher, (SearchableTreeNode) node.getChildAt(i));
            foundInChildren = searchInNode(searcher, (SearchableTreeNode) node.getChildAt(i)) || // Must be the last in condition
            foundInChildren;
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