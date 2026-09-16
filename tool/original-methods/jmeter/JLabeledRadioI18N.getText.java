/**
 * The implementation will get the resource name from the selected radio button
 * in the JButtonGroup.
 */
@Override
public String getText() {
    return this.bGroup.getSelection().getActionCommand();
}