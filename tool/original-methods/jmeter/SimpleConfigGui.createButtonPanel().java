/**
 * Create a panel containing the add and delete buttons.
 *
 * @return a GUI panel containing the buttons
 */
private JPanel createButtonPanel() {
    // A button for adding new parameters to the table.
    //$NON-NLS-1$
    JButton add = new JButton(JMeterUtils.getResString("add"));
    add.setActionCommand(ADD);
    add.addActionListener(this);
    add.setEnabled(true);
    // $NON-NLS-1$
    delete = new JButton(JMeterUtils.getResString("delete"));
    delete.setActionCommand(DELETE);
    delete.addActionListener(this);
    checkDeleteStatus();
    JPanel buttonPanel = new JPanel();
    buttonPanel.add(add);
    buttonPanel.add(delete);
    return buttonPanel;
}