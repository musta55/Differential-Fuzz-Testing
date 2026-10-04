/**
 * Handle the "about" action by displaying the "About Apache JMeter..."
 * dialog box. The Dialog Box is NOT modal, because those should be avoided
 * if at all possible.
 */
@Override
public void doAction(ActionEvent e) {
    if (e.getActionCommand().equals(ActionNames.ABOUT)) {
        showAboutDialog();
    }
}
// ---- helper method(s) introduced by the refactoring ----
/**
 * Called by about button. Raises about dialog. Currently the about box has
 * the product image and the copyright notice. The dialog box is centered
 * over the MainFrame.
 */
private static void showAboutDialog() {
    JFrame mainFrame = GuiPackage.getInstance().getMainFrame();
    JDialog dialog = createAboutDialog(mainFrame);
    centerDialog(mainFrame, dialog);
    dialog.setVisible(true);
}

/**
 * Centers the dialog over the main frame.
 *
 * @param mainFrame the main frame
 * @param dialog the dialog to center
 */
private static void centerDialog(JFrame mainFrame, JDialog dialog) {
    Point p = mainFrame.getLocationOnScreen();
    Dimension d1 = mainFrame.getSize();
    Dimension d2 = dialog.getSize();
    dialog.setLocation(p.x + (d1.width - d2.width) / 2, p.y + (d1.height - d2.height) / 2);
}

/**
 * Creates and initializes the about dialog.
 *
 * @param mainFrame the main frame
 * @return the initialized about dialog
 */
private static JDialog createAboutDialog(JFrame mainFrame) {
    if (about != null) {
        return about;
    }
    about = new EscapeDialog(mainFrame, "About Apache JMeter", false);
    addCloseMouseListener(about);
    JPanel infoPanel = createInfoPanel();
    setupDialogLayout(about, infoPanel);
    return about;
}

/**
 * Adds a mouse listener to close the dialog on mouse click.
 *
 * @param dialog the dialog to which the mouse listener is added
 */
private static void addCloseMouseListener(JDialog dialog) {
    dialog.addMouseListener(new MouseAdapter() {

        @Override
        public void mouseClicked(MouseEvent e) {
            dialog.setVisible(false);
        }
    });
}

/**
 * Creates the info panel containing copyright, rights, version, and release notes.
 *
 * @return the info panel
 */
private static JPanel createInfoPanel() {
    JPanel infos = new JPanel();
    infos.setOpaque(false);
    infos.setLayout(new GridLayout(0, 1));
    infos.setBorder(new EmptyBorder(5, 5, 5, 5));
    infos.add(new JLabel(JMeterUtils.getJMeterCopyright(), SwingConstants.CENTER));
    infos.add(new JLabel("All Rights Reserved.", SwingConstants.CENTER));
    infos.add(new JLabel("Apache JMeter Version " + JMeterUtils.getJMeterVersion(), SwingConstants.CENTER));
    JLabel releaseNotes = createReleaseNotesLabel();
    infos.add(releaseNotes);
    return infos;
}

/**
 * Creates the release notes label with a hyperlink.
 *
 * @return the release notes label
 */
private static JLabel createReleaseNotesLabel() {
    JLabel releaseNotes = new JLabel("<html><a href=\"https://jmeter.apache.org/changes.html\">Release notes</a></html>", SwingConstants.CENTER);
    releaseNotes.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    releaseNotes.addMouseListener(new MouseAdapter() {

        @Override
        public void mouseClicked(MouseEvent e) {
            if (e.getClickCount() > 0) {
                ActionRouter.getInstance().doActionNow(new ActionEvent(e.getSource(), e.getID(), ActionNames.LINK_RELEASE_NOTES));
            }
        }
    });
    return releaseNotes;
}

/**
 * Sets up the layout of the dialog.
 *
 * @param dialog the dialog
 * @param infoPanel the info panel
 */
private static void setupDialogLayout(JDialog dialog, JPanel infoPanel) {
    Container panel = dialog.getContentPane();
    panel.setLayout(new BorderLayout());
    panel.add(SplashScreen.loadLogo(), BorderLayout.NORTH);
    panel.add(infoPanel, BorderLayout.SOUTH);
    dialog.pack();
}

