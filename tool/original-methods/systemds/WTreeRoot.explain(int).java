@Override
protected String explain(int level) {
    StringBuilder sb = new StringBuilder();
    // append node summary
    sb.append("ROOT : " + _root.toString());
    sb.append("\n");
    // append child nodes
    if (!_children.isEmpty())
        for (AWTreeNode n : _children) sb.append(n.explain(level + 1));
    return sb.toString();
}