/**
 * Binds the given key stokes to focus transfer actions.
 * @param c component for adding key strokes
 * @param focusForward keystroke for forward focus transfer, or null
 * @param focusBackward keystroke for backward focus transfer, or null
 */
static void bind(JComponent c, KeyStroke focusForward, KeyStroke focusBackward) {
    bindKeyStroke(c, focusForward, TRANSFER_FOCUS.getValue(Action.NAME), TRANSFER_FOCUS);
    bindKeyStroke(c, focusBackward, TRANSFER_FOCUS_BACKWARD.getValue(Action.NAME), TRANSFER_FOCUS_BACKWARD);
}
// ---- helper method(s) introduced by the refactoring ----
private static void bindKeyStroke(JComponent c, KeyStroke keyStroke, Object actionName, Action action) {
    if (keyStroke != null) {
        InputMap inputMap = c.getInputMap();
        ActionMap actionMap = c.getActionMap();
        inputMap.put(keyStroke, actionName);
        actionMap.put(actionName, action);
    }
}

private static void transferFocus(Object source) {
    if (source instanceof Component) {
        ((Component) source).transferFocus();
    }
}

private static void transferFocusBackward(Object source) {
    if (source instanceof Component) {
        ((Component) source).transferFocusBackward();
    }
}

