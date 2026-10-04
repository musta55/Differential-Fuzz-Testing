/**
 * @see org.apache.jmeter.gui.action.Command#doAction(ActionEvent)
 */
@Override
public void doAction(ActionEvent e) {
    if (USE_LOCAL_HELP) {
        JDialog dialog = initHelpWindow();
        // set the window visible immediately
        dialog.setVisible(true);
        /*
             * This means that a new page will be shown before rendering is complete,
             * however the correct location will be displayed.
             * Attempts to use a "page" PropertyChangeListener to detect when the page
             * has been loaded failed to work any better.
             */
        StringBuilder url = new StringBuilder();
        if (e.getSource() instanceof String[]) {
            String[] source = (String[]) e.getSource();
            url.append(source[0]).append('#').append(source[1]);
        } else {
            url.append(HELP_COMPONENTS).append('#').append(GuiPackage.getInstance().getTreeListener().getCurrentNode().getDocAnchor());
        }
        try {
            // N.B. this only reloads if necessary (ignores the reference)
            helpDoc.setPage(url.toString());
        } catch (IOException ioe) {
            log.error("Error setting page for url, {}", url, ioe);
            helpDoc.setText("<html><head><title>Problem loading help page</title>" + "<style><!--" + ".note { background-color: #ffeeee; border: 1px solid brown; }" + "div { padding: 10; margin: 10; }" + "--></style></head>" + "<body><div class='note'>" + "<h1>Problem loading help page</h1>" + "<div>Can't load url: &quot;<em>" + url.toString() + "</em>&quot;</div>" + "<div>See log for more info</div>" + "</body>");
        }
    } else {
        if (e.getSource() instanceof String[]) {
            ActionRouter.getInstance().doActionNow(new ActionEvent(e.getSource(), e.getID(), ActionNames.LINK_FUNC_REF));
        } else {
            String[] source = new String[] { ActionNames.LINK_COMP_REF, GuiPackage.getInstance().getTreeListener().getCurrentNode().getDocAnchor() };
            ActionRouter.getInstance().doActionNow(new ActionEvent(source, e.getID(), ActionNames.LINK_COMP_REF));
        }
    }
}