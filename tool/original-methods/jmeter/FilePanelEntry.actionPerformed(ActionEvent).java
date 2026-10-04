@Override
public void actionPerformed(ActionEvent e) {
    if (e.getActionCommand().equals(ACTION_BROWSE)) {
        JFileChooser chooser;
        if (filetypes == null || filetypes.length == 0) {
            chooser = FileDialoger.promptToOpenFile(filename.getText(), onlyDirectories);
        } else {
            chooser = FileDialoger.promptToOpenFile(filetypes, filename.getText(), onlyDirectories);
        }
        if (chooser != null && chooser.getSelectedFile() != null) {
            filename.setText(chooser.getSelectedFile().getPath());
            fireFileChanged();
        }
    } else {
        fireFileChanged();
    }
}