/**
 * Loads dragged file asking before for save if current open file is dirty.
 * @param e {@link ActionEvent}
 * @param file File to Load
 */
public static void loadProject(ActionEvent e, File file) {
    if (!Close.performAction(e)) {
        return;
    }
    Load.loadProjectFile(e, file, false);
}