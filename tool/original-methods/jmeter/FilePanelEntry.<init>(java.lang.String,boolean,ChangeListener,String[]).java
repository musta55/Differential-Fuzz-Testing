public FilePanelEntry(String label, boolean onlyDirectories, ChangeListener listener, String... exts) {
    this.label = new JLabel(label);
    if (listener != null) {
        listeners.add(listener);
    }
    if (exts != null && // String null is converted to String[]{null} NOSONAR it's not code
    !(exts.length == 1 && exts[0] == null)) {
        this.filetypes = new String[exts.length];
        System.arraycopy(exts, 0, this.filetypes, 0, exts.length);
    } else {
        this.filetypes = null;
    }
    this.onlyDirectories = onlyDirectories;
    init();
}