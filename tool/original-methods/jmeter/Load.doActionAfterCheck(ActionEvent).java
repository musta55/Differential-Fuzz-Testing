@Override
public void doActionAfterCheck(final ActionEvent e) {
    //$NON-NLS-1$
    final JFileChooser chooser = FileDialoger.promptToOpenFile(new String[] { ".jmx" });
    if (chooser == null) {
        return;
    }
    final File selectedFile = chooser.getSelectedFile();
    if (selectedFile != null) {
        final boolean merging = e.getActionCommand().equals(ActionNames.MERGE);
        // We must ask the user if it is ok to close current project
        if (!merging) {
            // i.e. it is OPEN
            if (!Close.performAction(e)) {
                return;
            }
        }
        loadProjectFile(e, selectedFile, merging);
    }
}