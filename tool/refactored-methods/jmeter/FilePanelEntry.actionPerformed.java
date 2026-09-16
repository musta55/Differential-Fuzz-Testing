@Override
public void actionPerformed(ActionEvent e) {
    if (ACTION_BROWSE.equals(e.getActionCommand())) {
        JFileChooser chooser = filetypes == null || filetypes.length == 0 ? FileDialoger.promptToOpenFile(filename.getText(), onlyDirectories) : FileDialoger.promptToOpenFile(filetypes, filename.getText(), onlyDirectories);
        if (chooser != null && chooser.getSelectedFile() != null) {
            filename.setText(chooser.getSelectedFile().getPath());
            fireFileChanged();
        }
    } else {
        fireFileChanged();
    }
}