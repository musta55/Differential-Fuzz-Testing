@Override
protected String explain(int level) {
    StringBuilder sb = new StringBuilder();
    appendNodeSummary(sb);
    appendChildNodes(sb, level);
    return sb.toString();
}
// ---- helper method(s) introduced by the refactoring ----
public void setDecompressingToTrue() {
    isDecompressing = true;
}

private void appendNodeSummary(StringBuilder sb) {
    sb.append("ROOT : " + _root.toString());
    sb.append("\n");
}

private void appendChildNodes(StringBuilder sb, int level) {
    if (!_children.isEmpty()) {
        for (AWTreeNode n : _children) {
            sb.append(n.explain(level + 1));
        }
    }
}

