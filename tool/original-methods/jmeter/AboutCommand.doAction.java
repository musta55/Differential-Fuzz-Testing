/**
 * Handle the "about" action by displaying the "About Apache JMeter..."
 * dialog box. The Dialog Box is NOT modal, because those should be avoided
 * if at all possible.
 */
@Override
public void doAction(ActionEvent e) {
    if (e.getActionCommand().equals(ActionNames.ABOUT)) {
        AboutCommand.about();
    }
}