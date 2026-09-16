/**
 * Loads or merges a file into the current GUI, reporting any errors to the user.
 * If the file is a complete test plan, sets the GUI test plan file name
 *
 * @param e the event that triggered the action
 * @param f the file to load
 * @param merging if true, then try to merge the file into the current GUI.
 * @param setDetails if true, then set the file details (if not merging)
 */
static void loadProjectFile(final ActionEvent e, final File f, final boolean merging, final boolean setDetails) {
    ActionRouter.getInstance().doActionNow(new ActionEvent(e.getSource(), e.getID(), ActionNames.STOP_THREAD));
    final GuiPackage guiPackage = GuiPackage.getInstance();
    if (f != null) {
        try {
            if (merging) {
                log.info("Merging file: {}", f);
            } else {
                log.info("Loading file: {}", f);
                // TODO should this be done even if not a full test plan?
                // and what if load fails?
                if (setDetails) {
                    FileServer.getFileServer().setBaseForScript(f);
                }
            }
            final HashTree tree = SaveService.loadTree(f);
            final boolean isTestPlan = insertLoadedTree(e.getID(), tree, merging);
            // don't change name if merging
            if (!merging && isTestPlan && setDetails) {
                // TODO should setBaseForScript be called here rather than
                // above?
                guiPackage.setTestPlanFile(f.getAbsolutePath());
            }
        } catch (NoClassDefFoundError ex) {
            // Allow for missing optional jars
            reportError("Missing jar file. {}", ex, true);
        } catch (ConversionException ex) {
            if (log.isWarnEnabled()) {
                log.warn("Could not convert file. {}", ex.toString());
            }
            JMeterUtils.reportErrorToUser(SaveService.CEtoString(ex));
        } catch (IOException ex) {
            reportError("Error reading file. {}", ex, false);
        } catch (StreamException ex) {
            Throwable exceptionToDisplay = ex;
            if ("".equals(ex.getMessage()) && ex.getCause() != null) {
                exceptionToDisplay = ex.getCause();
            }
            reportError("Error in XML format. {}", exceptionToDisplay, false);
        } catch (Exception ex) {
            reportError("Unexpected error. {}", ex, true);
        }
        FileDialoger.setLastJFCDirectory(f.getParentFile().getAbsolutePath());
        guiPackage.updateCurrentGui();
        guiPackage.getMainFrame().repaint();
    }
}