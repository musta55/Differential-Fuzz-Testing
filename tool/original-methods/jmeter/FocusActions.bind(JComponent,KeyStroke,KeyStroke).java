/**
 * Binds the given key stokes to focus transfer actions.
 * @param c component for adding key strokes
 * @param focusForward keystroke for forward focus transfer, or null
 * @param focusBackward keystroke for backward focus transfer, or null
 */
static void bind(JComponent c, KeyStroke focusForward, KeyStroke focusBackward) {
    Object transferFocusName = TRANSFER_FOCUS.getValue(Action.NAME);
    Object transferFocusBackward = TRANSFER_FOCUS_BACKWARD.getValue(Action.NAME);
    InputMap inputMap = c.getInputMap();
    ActionMap actionMap = c.getActionMap();
    if (focusForward != null) {
        inputMap.put(focusForward, transferFocusName);
        actionMap.put(transferFocusName, TRANSFER_FOCUS);
    }
    if (focusBackward != null) {
        inputMap.put(focusBackward, transferFocusBackward);
        actionMap.put(transferFocusBackward, TRANSFER_FOCUS_BACKWARD);
    }
}