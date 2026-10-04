/**
 * Provide the list of Action names that are available in this command.
 */
@Override
public Set<String> getActionNames() {
    return commandSet;
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

