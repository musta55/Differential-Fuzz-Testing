/*
	 * Function to compute the node level in the entire Lops DAG. 
	 *   level(v) = max( levels(v.inputs) ) + 1
	 */
public void setLevel(ArrayList<Lop> inputs) {
    int tmplevel = computeMaxInputLevel(inputs);
    setLevel(tmplevel + 1);
}
// ---- helper method(s) introduced by the refactoring ----
private int computeMaxInputLevel(ArrayList<Lop> inputs) {
    if (inputs == null || inputs.isEmpty()) {
        return 0;
    }
    int maxLevel = -1;
    for (Lop in : inputs) {
        maxLevel = Math.max(maxLevel, in.getLevel());
    }
    return maxLevel;
}

