@Override
public void actionPerformed(ActionEvent e) {
    if (e.getSource() == clear) {
        handleClearAction();
    } else if (e.getActionCommand().equals(ACTION_BROWSE)) {
        handleBrowseAction();
    } else if (e.getSource() == delete) {
        handleDeleteAction();
    } else {
        fireFileChanged();
    }
}
// ---- helper method(s) introduced by the refactoring ----
private void handleClearAction() {
    this.clearFiles();
}

private void handleBrowseAction() {
    JFileChooser chooser = new JFileChooser();
    // $NON-NLS-1$ // $NON-NLS-2$
    String start = System.getProperty("user.dir", "");
    chooser.setCurrentDirectory(new File(start));
    chooser.setFileFilter(new JMeterFileFilter(new String[] { filetype }));
    chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
    chooser.setMultiSelectionEnabled(true);
    if (chooser.showOpenDialog(GuiPackage.getInstance().getMainFrame()) != JFileChooser.APPROVE_OPTION) {
        return;
    }
    File[] cfiles = chooser.getSelectedFiles();
    if (cfiles != null) {
        for (File cfile : cfiles) {
            this.addFilename(cfile.getPath());
        }
        fireFileChanged();
    }
}

private void handleDeleteAction() {
    this.deleteFile();
}

