/**
 * @see org.apache.jmeter.gui.action.Command#doAction(ActionEvent)
 */
@Override
public void doAction(ActionEvent e) {
    String url = LINK_MAP.get(e.getActionCommand());
    if (url == null) {
        log.warn("Action {} not handled by this class", e.getActionCommand());
        return;
    }
    try {
        if (e.getSource() instanceof String[]) {
            url += "#" + ((String[]) e.getSource())[1];
        }
        java.awt.Desktop.getDesktop().browse(java.net.URI.create(url));
    } catch (IOException err) {
        log.error("OpenLinkAction: User default browser is not found, or it fails to be launched," + " or the default handler application failed to be launched on {}", url, err);
    } catch (UnsupportedOperationException err) {
        log.error("OpenLinkAction: Current platform does not support the Desktop.Action.BROWSE action on {}", url, err);
        showBrowserWarning(url);
    } catch (SecurityException err) {
        log.error("OpenLinkAction: Security problem on {}", url, err);
    } catch (Exception err) {
        log.error("OpenLinkAction on {}", url, err);
    }
}