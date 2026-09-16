/**
 * The implementation will get the resource name from the selected radio button
 * in the JButtonGroup.
 */
@Override
public String getText() {
    ButtonModel selection = bGroup.getSelection();
    return selection != null ? selection.getActionCommand() : null;
}