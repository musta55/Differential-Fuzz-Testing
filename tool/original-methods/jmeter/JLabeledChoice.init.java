/**
 * Initialises all of the components on this panel.
 */
private void init() {
    // WARNING: called from ctor so must not be overridden (i.e. must be private or final)
    setLayout(new BorderLayout(5, 0));
    // Register the handler for focus listening. This handler will
    // only notify the registered when the text changes from when
    // the focus is gained to when it is lost.
    choiceList.addItemListener(e -> {
        if (e.getStateChange() == ItemEvent.SELECTED) {
            notifyChangeListeners();
        }
    });
    // Add the sub components
    this.add(mLabel);
    this.add(choiceList);
    if (withButtons) {
        add = new JButton("Add");
        add.setMargin(new Insets(1, 1, 1, 1));
        add.addActionListener(new AddListener());
        this.add(add);
        delete = new JButton("Del");
        delete.setMargin(new Insets(1, 1, 1, 1));
        delete.addActionListener(new DeleteListener());
        this.add(delete);
    }
}