/**
 * Handle the "sslmanager" action by displaying the "SSL CLient Manager"
 * dialog box. The Dialog Box is NOT modal, because those should be avoided
 * if at all possible.
 */
@Override
public void doAction(ActionEvent e) {
    if (e.getActionCommand().equals(ActionNames.SSL_MANAGER)) {
        SSLManagerCommand.sslManager();
    }
}