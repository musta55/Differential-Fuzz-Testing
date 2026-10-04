@Override
public void actionPerformed(ActionEvent e) {
    final String item = serverTypeBox.getText();
    if (item.equals("pop3") || item.equals("pop3s")) {
        folderLabel.setEnabled(false);
        folderBox.setText(INBOX);
        folderBox.setEnabled(false);
    } else {
        folderLabel.setEnabled(true);
        folderBox.setEnabled(true);
    }
}