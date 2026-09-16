private void createDialogBox() {
    JFrame mainFrame = GuiPackage.getInstance().getMainFrame();
    String title = //$NON-NLS-1$
    editable ? //$NON-NLS-1$
    JMeterUtils.getResString("textbox_title_edit") : //$NON-NLS-1$
    JMeterUtils.getResString("textbox_title_view");
    // modal dialog box
    dialog = new JDialog(mainFrame, title, true);
    // Close action dialog box when tapping Escape key
    JPanel content = (JPanel) dialog.getContentPane();
    content.registerKeyboardAction(this, KeyStrokes.ESC, JComponent.WHEN_IN_FOCUSED_WINDOW);
    textBox = new JEditorPane();
    textBox.setEditable(editable);
    JScrollPane textBoxScrollPane = GuiUtils.makeScrollPane(textBox);
    JPanel btnBar = new JPanel();
    btnBar.setLayout(new FlowLayout(FlowLayout.RIGHT));
    if (editable) {
        //$NON-NLS-1$
        JButton cancelBtn = new JButton(JMeterUtils.getResString("textbox_cancel"));
        cancelBtn.setActionCommand(CANCEL_COMMAND);
        cancelBtn.addActionListener(this);
        //$NON-NLS-1$
        JButton saveBtn = new JButton(JMeterUtils.getResString("textbox_save_close"));
        saveBtn.setActionCommand(SAVE_CLOSE_COMMAND);
        saveBtn.addActionListener(this);
        btnBar.add(cancelBtn);
        btnBar.add(saveBtn);
    } else {
        //$NON-NLS-1$
        JButton closeBtn = new JButton(JMeterUtils.getResString("textbox_close"));
        closeBtn.setActionCommand(CLOSE_COMMAND);
        closeBtn.addActionListener(this);
        btnBar.add(closeBtn);
    }
    // Prepare dialog box
    Container panel = dialog.getContentPane();
    dialog.setMinimumSize(new Dimension(400, 250));
    panel.add(textBoxScrollPane, BorderLayout.CENTER);
    panel.add(btnBar, BorderLayout.SOUTH);
    // determine location on screen
    Point p = mainFrame.getLocationOnScreen();
    Dimension d1 = mainFrame.getSize();
    Dimension d2 = dialog.getSize();
    dialog.setLocation(p.x + (d1.width - d2.width) / 2, p.y + (d1.height - d2.height) / 2);
    dialog.pack();
}