/**
 * This method is called from errors-only checkbox
 *
 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
 */
@Override
public void actionPerformed(ActionEvent e) {
    if (errorsOnly.isSelected()) {
        warningThresholdField.setEnabled(false);
        warningThresholdField.setEditable(false);
    } else {
        warningThresholdField.setEnabled(true);
        warningThresholdField.setEditable(true);
    }
}