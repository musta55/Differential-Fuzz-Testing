/**
 * Explain tool: prints the hierarchical plan (including all available
 * detail information, if necessary) to <code>stdout</code>.
 *
 * @param withDetails if true, include explain details
 * @return string explanation
 */
public String explain(boolean withDetails) {
    StringBuilder sb = new StringBuilder();
    sb.append("\n");
    sb.append("----------------------------\n");
    sb.append(" EXPLAIN OPT TREE (type=HOPS, size=");
    sb.append(_root.size());
    sb.append(")\n");
    sb.append("----------------------------\n");
    sb.append(_root.explain(1, withDetails));
    sb.append("----------------------------\n");
    return sb.toString();
}