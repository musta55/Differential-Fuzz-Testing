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
    initLayout();
    initComponents();
}
// ---- helper method(s) introduced by the refactoring ----
private void initLayout() {
    setLayout(new BorderLayout(0, 5));
    if (displayName) {
        setBorder(makeBorder());
        add(makeTitlePanel(), BorderLayout.NORTH);
    }
}

private void initComponents() {
    VerticalPanel mainPanel = new VerticalPanel();
    mainPanel.add(createUsernamePanel());
    mainPanel.add(createPasswordPanel());
    add(mainPanel, BorderLayout.CENTER);
}

