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
        add.addActionListener(e -> handleAddAction());
        this.add(add);
        delete = new JButton("Del");
        delete.setMargin(new Insets(1, 1, 1, 1));
        delete.addActionListener(e -> handleDeleteAction());
        this.add(delete);
    }
}
// ---- helper method(s) introduced by the refactoring ----
private void handleAddAction() {
    String item = (String) choiceList.getSelectedItem();
    int index = choiceList.getSelectedIndex();
    if (!item.equals(choiceList.getItemAt(index))) {
        choiceList.addItem(item);
    }
    choiceList.setSelectedItem(item);
    notifyChangeListeners();
}

private void handleDeleteAction() {
    if (choiceList.getItemCount() > 1) {
        choiceList.removeItemAt(choiceList.getSelectedIndex());
        notifyChangeListeners();
    }
}

