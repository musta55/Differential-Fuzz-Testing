/**
 * @see org.apache.jmeter.gui.action.Command#doAction(ActionEvent)
 */
@Override
public void doAction(ActionEvent e) {
    if (USE_LOCAL_HELP) {
        JDialog dialog = initHelpWindow();
        // set the window visible immediately
        dialog.setVisible(true);
        String url = buildHelpUrl(e);
        try {
            // N.B. this only reloads if necessary (ignores the reference)
            helpDoc.setPage(url);
        } catch (IOException ioe) {
            log.error("Error setting page for url, {}", url, ioe);
            helpDoc.setText(buildErrorMessage(url));
        }
    } else {
        handleRemoteHelp(e);
    }
}
// ---- helper method(s) introduced by the refactoring ----
private static String buildHelpUrl(ActionEvent e) {
    StringBuilder url = new StringBuilder();
    if (e.getSource() instanceof String[]) {
        String[] source = (String[]) e.getSource();
        url.append(source[0]).append('#').append(source[1]);
    } else {
        url.append(HELP_COMPONENTS).append('#').append(GuiPackage.getInstance().getTreeListener().getCurrentNode().getDocAnchor());
    }
    return url.toString();
}

private static String buildErrorMessage(String url) {
    return "<html><head><title>Problem loading help page</title>" + "<style><!--" + ".note { background-color: #ffeeee; border: 1px solid brown; }" + "div { padding: 10; margin: 10; }" + "--></style></head>" + "<body><div class='note'>" + "<h1>Problem loading help page</h1>" + "<div>Can't load url: &quot;<em>" + url + "</em>&quot;</div>" + "<div>See log for more info</div>" + "</body>";
}

private static void handleRemoteHelp(ActionEvent e) {
    if (e.getSource() instanceof String[]) {
        ActionRouter.getInstance().doActionNow(new ActionEvent(e.getSource(), e.getID(), ActionNames.LINK_FUNC_REF));
    } else {
        String[] source = new String[] { ActionNames.LINK_COMP_REF, GuiPackage.getInstance().getTreeListener().getCurrentNode().getDocAnchor() };
        ActionRouter.getInstance().doActionNow(new ActionEvent(source, e.getID(), ActionNames.LINK_COMP_REF));
    }
}

