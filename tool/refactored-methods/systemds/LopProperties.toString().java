@Override
public String toString() {
    return String.format("%s ID: %d Level: %d ExecType: %s Intermediate: %b", this.getClass().getSimpleName(), ID, level, execType, producesIntermediateOutput);
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

