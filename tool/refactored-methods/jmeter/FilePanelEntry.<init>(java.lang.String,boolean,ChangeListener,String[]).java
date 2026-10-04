public FilePanelEntry(String label, boolean onlyDirectories, ChangeListener listener, String... exts) {
    this.label = new JLabel(label);
    if (listener != null) {
        listeners.add(listener);
    }
    // NOSONAR
    this.filetypes = exts != null && !(exts.length == 1 && exts[0] == null) ? exts.clone() : null;
    this.onlyDirectories = onlyDirectories;
    init();
}