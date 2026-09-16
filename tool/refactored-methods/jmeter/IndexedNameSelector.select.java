/*
     * (non-Javadoc)
     *
     * @see
     * org.apache.jmeter.report.csv.processor.SampleSelector#select(org.apache
     * .jmeter.report.csv.core.Sample)
     */
@Override
public Double select(Sample sample) {
    String name = sample.getName();
    int index = findNameIndex(name);
    if (index < 0) {
        index = addName(name);
    }
    return (double) index;
}
// ---- helper method(s) introduced by the refactoring ----
/**
 * Finds the index of the given name in the list of names.
 *
 * @param name the name to find
 * @return the index of the name, or -1 if not found
 */
private int findNameIndex(String name) {
    return names.indexOf(name);
}

/**
 * Adds a new name to the list and returns its index.
 *
 * @param name the name to add
 * @return the index of the newly added name
 */
private int addName(String name) {
    names.add(name);
    return names.size() - 1;
}

