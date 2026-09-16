/**
 * Helper routine to allow action to be shared by LOAD.
 *
 * @param e event
 * @return true if Close was not cancelled
 */
static boolean performAction(ActionEvent e) {
    ActionRouter.getInstance().doActionNow(new ActionEvent(e.getSource(), e.getID(), ActionNames.CHECK_DIRTY));
    GuiPackage guiPackage = GuiPackage.getInstance();
    if (guiPackage.isDirty()) {
        int response;
        if ((response = JOptionPane.showConfirmDialog(GuiPackage.getInstance().getMainFrame(), // $NON-NLS-1$
        JMeterUtils.getResString("cancel_new_to_save"), // $NON-NLS-1$
        JMeterUtils.getResString("save?"), JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE)) == JOptionPane.YES_OPTION) {
            ActionRouter.getInstance().doActionNow(new ActionEvent(e.getSource(), e.getID(), ActionNames.SAVE));
            // the user might cancel the file chooser dialog
            // in this case we should not close the test plan
            if (guiPackage.isDirty()) {
                return false;
            }
        }
        if (response == JOptionPane.CLOSED_OPTION || response == JOptionPane.CANCEL_OPTION) {
            // Don't clear the plan
            return false;
        }
    }
    ActionRouter.getInstance().doActionNow(new ActionEvent(e.getSource(), e.getID(), ActionNames.STOP_THREAD));
    closeProject(e);
    return true;
}