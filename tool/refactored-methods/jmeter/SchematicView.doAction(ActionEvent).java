/**
 * @see Command#doAction(ActionEvent)
 */
@Override
public void doAction(ActionEvent e) {
    try {
        String updateFile = GuiPackage.getInstance().getTestPlanFile();
        if (updateFile != null) {
            ActionRouter.getInstance().doActionNow(new ActionEvent(e.getSource(), e.getID(), ActionNames.CHECK_DIRTY));
            File outputFile = chooseOutputFile(updateFile);
            if (outputFile != null) {
                try (FileOutputStream fileOutputStream = new FileOutputStream(outputFile);
                    BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(fileOutputStream)) {
                    new XslSchematicViewGenerator().generate(GuiPackage.getInstance().getCurrentSubTree(), new File(updateFile), bufferedOutputStream);
                }
                JMeterUtils.reportInfoToUser(GENERATION_SUCCESS_MSG.format(new Object[] { outputFile.getAbsolutePath() }), JMeterUtils.getResString("schematic_view_info"));
            }
        } else {
            JMeterUtils.reportInfoToUser(JMeterUtils.getResString("schematic_view_no_plan"), JMeterUtils.getResString("schematic_view_info"));
        }
    } catch (Exception ex) {
        JMeterUtils.reportErrorToUser(JMeterUtils.getResString("schematic_view_errors"), ex);
    }
}
// ---- helper method(s) introduced by the refactoring ----
private static File chooseOutputFile(String updateFile) {
    JFileChooser jFileChooser = new JFileChooser();
    jFileChooser.setDialogTitle(JMeterUtils.getResString("schematic_view_outputfile"));
    jFileChooser.setCurrentDirectory(new File(updateFile).getParentFile());
    jFileChooser.setSelectedFile(new File(updateFile + ".html"));
    int retVal = jFileChooser.showSaveDialog(GuiPackage.getInstance().getMainFrame());
    if (retVal == JFileChooser.APPROVE_OPTION) {
        File outputFile = jFileChooser.getSelectedFile();
        if (outputFile.exists()) {
            int response = JOptionPane.showConfirmDialog(GuiPackage.getInstance().getMainFrame(), // $NON-NLS-1$
            JMeterUtils.getResString("save_overwrite_existing_file"), // $NON-NLS-1$
            JMeterUtils.getResString("save?"), JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (response == JOptionPane.CLOSED_OPTION || response == JOptionPane.NO_OPTION) {
                // Do not save, user does not want to overwrite
                return null;
            }
        }
        return outputFile;
    }
    return null;
}

