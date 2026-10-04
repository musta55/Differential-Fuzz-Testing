private JPanel createClosePortPanel() {
    //$NON-NLS-1$
    JLabel label = new JLabel(JMeterUtils.getResString("reuseconnection"));
    reUseConnection = new JCheckBox("", true);
    reUseConnection.addItemListener(e -> {
        if (e.getStateChange() == ItemEvent.SELECTED) {
            closeConnection.setEnabled(true);
        } else {
            closeConnection.setEnabled(false);
        }
    });
    label.setLabelFor(reUseConnection);
    JPanel closePortPanel = new JPanel(new FlowLayout());
    closePortPanel.add(label);
    closePortPanel.add(reUseConnection);
    return closePortPanel;
}