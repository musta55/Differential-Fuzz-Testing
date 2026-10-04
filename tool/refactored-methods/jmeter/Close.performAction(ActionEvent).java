/**
 * Helper routine to allow action to be shared by LOAD.
 *
 * @param e event
 * @return true if Close was not cancelled
 */
static boolean performAction(ActionEvent e) {
    checkDirtyState(e);
    if (GuiPackage.getInstance().isDirty()) {
        int response = showSaveConfirmationDialog();
        if (response == JOptionPane.YES_OPTION) {
            saveAndCheckDirtyState(e);
            if (GuiPackage.getInstance().isDirty()) {
                return false;
            }
        }
        if (response == JOptionPane.CLOSED_OPTION || response == JOptionPane.CANCEL_OPTION) {
            // Don't clear the plan
            return false;
        }
    }
    stopThreadsAndCloseProject(e);
    return true;
}
// ---- helper method(s) introduced by the refactoring ----
private static void checkDirtyState(ActionEvent e) {
    ActionRouter.getInstance().doActionNow(new ActionEvent(e.getSource(), e.getID(), ActionNames.CHECK_DIRTY));
}

private static int showSaveConfirmationDialog() {
    return JOptionPane.showConfirmDialog(GuiPackage.getInstance().getMainFrame(), // $NON-NLS-1$
    JMeterUtils.getResString("cancel_new_to_save"), // $NON-NLS-1$
    JMeterUtils.getResString("save?"), JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
}

private static void saveAndCheckDirtyState(ActionEvent e) {
    ActionRouter.getInstance().doActionNow(new ActionEvent(e.getSource(), e.getID(), ActionNames.SAVE));
}

private static void stopThreadsAndCloseProject(ActionEvent e) {
    ActionRouter.getInstance().doActionNow(new ActionEvent(e.getSource(), e.getID(), ActionNames.STOP_THREAD));
    closeProject(e);
}

