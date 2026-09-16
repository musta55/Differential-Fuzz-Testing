private void init(String text) {
    createDialogBox();
    setTextBox(text);
    showDialog();
}
// ---- helper method(s) introduced by the refactoring ----
private String getTitle() {
    return editable ? JMeterUtils.getResString("textbox_title_edit") : JMeterUtils.getResString("textbox_title_view");
}

private void setupCloseAction() {
    JPanel content = (JPanel) dialog.getContentPane();
    content.registerKeyboardAction(this, KeyStrokes.ESC, JComponent.WHEN_IN_FOCUSED_WINDOW);
}

private void setupTextBox() {
    textBox = new JEditorPane();
    textBox.setEditable(editable);
    JScrollPane textBoxScrollPane = GuiUtils.makeScrollPane(textBox);
    dialog.getContentPane().add(textBoxScrollPane, BorderLayout.CENTER);
}

private void setupButtonBar() {
    JPanel btnBar = new JPanel();
    btnBar.setLayout(new FlowLayout(FlowLayout.RIGHT));
    addButtons(btnBar);
    dialog.getContentPane().add(btnBar, BorderLayout.SOUTH);
}

private void addButtons(JPanel btnBar) {
    if (editable) {
        addButton(btnBar, JMeterUtils.getResString("textbox_cancel"), CANCEL_COMMAND);
        addButton(btnBar, JMeterUtils.getResString("textbox_save_close"), SAVE_CLOSE_COMMAND);
    } else {
        addButton(btnBar, JMeterUtils.getResString("textbox_close"), CLOSE_COMMAND);
    }
}

private void addButton(JPanel btnBar, String label, String actionCommand) {
    JButton button = new JButton(label);
    button.setActionCommand(actionCommand);
    button.addActionListener(this);
    btnBar.add(button);
}

private void setupDialogLocation(JFrame mainFrame) {
    Point p = mainFrame.getLocationOnScreen();
    Dimension d1 = mainFrame.getSize();
    Dimension d2 = dialog.getSize();
    dialog.setLocation(p.x + (d1.width - d2.width) / 2, p.y + (d1.height - d2.height) / 2);
    dialog.setMinimumSize(new Dimension(400, 250));
    dialog.pack();
}

private void showDialog() {
    dialog.setVisible(true);
}

