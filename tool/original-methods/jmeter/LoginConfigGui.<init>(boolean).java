/**
 * Create a new LoginConfigGui as either a standalone or an embedded
 * component.
 *
 * @param displayName
 *            indicates whether or not this component should display its
 *            name. If true, this is a standalone component. If false, this
 *            component is intended to be used as a subpanel for another
 *            component.
 */
public LoginConfigGui(boolean displayName) {
    this.displayName = displayName;
    init();
}