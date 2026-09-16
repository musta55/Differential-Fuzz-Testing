/**
 * Description of the Method
 *
 * @param e
 *            Description of Parameter
 */
@Override
public void doActionAfterCheck(ActionEvent e) {
    ActionRouter.getInstance().doActionNow(new ActionEvent(e.getSource(), e.getID(), ActionNames.CHECK_DIRTY));
    GuiPackage guiPackage = GuiPackage.getInstance();
    if (guiPackage.isDirty()) {
        int chosenOption = JOptionPane.showConfirmDialog(guiPackage.getMainFrame(), JMeterUtils.getResString(// $NON-NLS-1$
        "cancel_exit_to_save"), // $NON-NLS-1$
        JMeterUtils.getResString("save?"), JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (chosenOption == JOptionPane.NO_OPTION) {
            System.exit(0);
        } else if (chosenOption == JOptionPane.YES_OPTION) {
            ActionRouter.getInstance().doActionNow(new ActionEvent(e.getSource(), e.getID(), ActionNames.SAVE));
            if (!guiPackage.isDirty()) {
                System.exit(0);
            }
        }
    } else {
        System.exit(0);
    }
}