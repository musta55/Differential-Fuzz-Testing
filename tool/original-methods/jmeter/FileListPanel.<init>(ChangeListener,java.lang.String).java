/**
 * Constructor for the FilePanel object.
 * @param l The changelistener for this panel
 * @param title The title of this panel
 */
public FileListPanel(ChangeListener l, String title) {
    this.title = title;
    init();
    listeners.add(l);
}