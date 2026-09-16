/**
 * Handle the "sslmanager" action by displaying the "SSL CLient Manager"
 * dialog box. The Dialog Box is NOT modal, because those should be avoided
 * if at all possible.
 */
@Override
public void doAction(ActionEvent e) {
    if (ActionNames.SSL_MANAGER.equals(e.getActionCommand())) {
        showSSLManagerDialog();
    }
}
// ---- helper method(s) introduced by the refactoring ----
/**
 * Called by sslManager button. Raises sslManager dialog.
 * I.e. a FileChooser for PCSI12 (.p12|.P12) or JKS files.
 */
private static void showSSLManagerDialog() {
    SSLManager.reset();
    JFileChooser keyStoreChooser = createFileChooser();
    int retVal = keyStoreChooser.showOpenDialog(GuiPackage.getInstance().getMainFrame());
    if (JFileChooser.APPROVE_OPTION == retVal) {
        updateKeyStoreProperty(keyStoreChooser.getSelectedFile());
    }
    SSLManager.getInstance();
}

private static JFileChooser createFileChooser() {
    //$NON-NLS-1$
    JFileChooser keyStoreChooser = new JFileChooser(System.getProperty("user.dir"));
    keyStoreChooser.setDialogTitle(JMeterUtils.getResString("sslmanager.title"));
    keyStoreChooser.addChoosableFileFilter(new AcceptPKCS12OrJKSFileFilter());
    keyStoreChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
    return keyStoreChooser;
}

private static void updateKeyStoreProperty(File selectedFile) {
    try {
        System.setProperty(SSLManager.JAVAX_NET_SSL_KEY_STORE, selectedFile.getCanonicalPath());
    } catch (IOException e) {
        // Ignored
    }
}

