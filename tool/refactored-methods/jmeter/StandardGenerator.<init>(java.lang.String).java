/**
 * @param file name of a file (TODO seems not to be used anywhere)
 */
public StandardGenerator(String file) {
    FILENAME = file;
    initialize();
}
// ---- helper method(s) introduced by the refactoring ----
/**
 * Initialize the generator.
 */
private void initialize() {
    generateRequest();
}

