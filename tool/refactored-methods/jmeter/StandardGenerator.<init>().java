/**
 * The constructor is used by GUI and samplers to generate request objects.
 */
public StandardGenerator() {
    super();
    initialize();
}
// ---- helper method(s) introduced by the refactoring ----
/**
 * Initialize the generator.
 */
private void initialize() {
    generateRequest();
}

