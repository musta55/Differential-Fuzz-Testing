/**
 * Loads the dragged file, prompting to save the current project if it is dirty.
 *
 * @param actionEvent the action event triggering the load
 * @param fileToLoad the file to be loaded
 */
public static void loadProject(ActionEvent actionEvent, File fileToLoad) {
    if (!Close.performAction(actionEvent)) {
        return;
    }
    Load.loadProjectFile(actionEvent, fileToLoad, false);
}