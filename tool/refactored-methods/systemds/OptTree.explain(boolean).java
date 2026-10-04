/**
 * Explain tool: prints the hierarchical plan (including all available
 * detail information, if necessary) to <code>stdout</code>.
 *
 * @param withDetails if true, include explain details
 * @return string explanation
 */
public String explain(boolean withDetails) {
    StringBuilder sb = new StringBuilder();
    appendHeader(sb);
    appendRootExplanation(sb, withDetails);
    appendFooter(sb);
    return sb.toString();
}
// ---- helper method(s) introduced by the refactoring ----
private void appendHeader(StringBuilder sb) {
    sb.append("\n");
    sb.append("----------------------------\n");
    sb.append(" EXPLAIN OPT TREE (type=HOPS, size=");
    sb.append(_root.size());
    sb.append(")\n");
    sb.append("----------------------------\n");
}

private void appendRootExplanation(StringBuilder sb, boolean withDetails) {
    sb.append(_root.explain(1, withDetails));
}

private void appendFooter(StringBuilder sb) {
    sb.append("----------------------------\n");
}

