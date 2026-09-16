static void loadProjectFile(final ActionEvent e, final File f, final boolean merging, final boolean setDetails) {
    ActionRouter.getInstance().doActionNow(new ActionEvent(e.getSource(), e.getID(), ActionNames.STOP_THREAD));
    final GuiPackage guiPackage = GuiPackage.getInstance();
    if (f != null) {
        try {
            logFileAction(f, merging);
            if (setDetails && !merging) {
                FileServer.getFileServer().setBaseForScript(f);
            }
            final HashTree tree = SaveService.loadTree(f);
            final boolean isTestPlan = insertLoadedTree(e.getID(), tree, merging);
            if (!merging && isTestPlan && setDetails) {
                guiPackage.setTestPlanFile(f.getAbsolutePath());
            }
        } catch (NoClassDefFoundError ex) {
            reportError("Missing jar file. {}", ex, true);
        } catch (ConversionException ex) {
            logAndReportConversionError(ex);
        } catch (IOException ex) {
            reportError("Error reading file. {}", ex, false);
        } catch (StreamException ex) {
            reportStreamException(ex);
        } catch (Exception ex) {
            reportError("Unexpected error. {}", ex, true);
        }
        updateFileDialogerAndGui(f, guiPackage);
    }
}
// ---- helper method(s) introduced by the refactoring ----
private static void logFileAction(File f, boolean merging) {
    if (merging) {
        log.info("Merging file: {}", f);
    } else {
        log.info("Loading file: {}", f);
    }
}

private static void logAndReportConversionError(ConversionException ex) {
    if (log.isWarnEnabled()) {
        log.warn("Could not convert file. {}", ex.toString());
    }
    JMeterUtils.reportErrorToUser(SaveService.CEtoString(ex));
}

private static void reportStreamException(StreamException ex) {
    Throwable exceptionToDisplay = ex;
    if ("".equals(ex.getMessage()) && ex.getCause() != null) {
        exceptionToDisplay = ex.getCause();
    }
    reportError("Error in XML format. {}", exceptionToDisplay, false);
}

private static void updateFileDialogerAndGui(File f, GuiPackage guiPackage) {
    FileDialoger.setLastJFCDirectory(f.getParentFile().getAbsolutePath());
    guiPackage.updateCurrentGui();
    guiPackage.getMainFrame().repaint();
}

private static void validateTree(HashTree tree) throws IllegalUserActionException {
    if (tree == null) {
        throw new IllegalUserActionException("Empty TestPlan or error reading test plan - see log file");
    }
}

private static void validateMergeTarget(HashTree tree, GuiPackage guiInstance) throws IllegalUserActionException {
    final TestElement te = (TestElement) tree.getArray()[0];
    if (!(te instanceof TestPlan)) {
        final boolean ok = MenuFactory.canAddTo(guiInstance.getCurrentNode(), te);
        if (!ok) {
            reportIllegalMergeTarget(te);
        }
    }
}

private static void reportIllegalMergeTarget(TestElement te) throws IllegalUserActionException {
    String name = te.getName();
    String className = te.getClass().getName();
    className = className.substring(className.lastIndexOf('.') + 1);
    throw new IllegalUserActionException("Can't merge " + name + " (" + className + ") here");
}

private static void updateGuiAndSelectPath(int id, GuiPackage guiInstance, HashTree newTree, boolean merging) {
    guiInstance.updateCurrentGui();
    guiInstance.getMainFrame().getTree().setSelectionPath(new TreePath(((JMeterTreeNode) newTree.getArray()[0]).getPath()));
    final HashTree subTree = guiInstance.getCurrentSubTree();
    ActionEvent actionEvent = createActionEvent(subTree, id, merging);
    ActionRouter.getInstance().actionPerformed(actionEvent);
    expandTreeIfRequired(guiInstance.getMainFrame().getTree(), merging);
}

private static ActionEvent createActionEvent(HashTree subTree, int id, boolean merging) {
    return new ActionEvent(subTree.get(subTree.getArray()[subTree.size() - 1]), id, merging ? ActionNames.SUB_TREE_MERGED : ActionNames.SUB_TREE_LOADED);
}

private static void expandTreeIfRequired(JTree jTree, boolean merging) {
    if (EXPAND_TREE && !merging) {
        expandAllRows(jTree);
    } else {
        jTree.expandRow(0);
    }
    jTree.setSelectionPath(jTree.getPathForRow(1));
    FocusRequester.requestFocus(jTree);
}

private static void expandAllRows(JTree jTree) {
    for (int i = 0; i < jTree.getRowCount(); i++) {
        jTree.expandRow(i);
    }
}

