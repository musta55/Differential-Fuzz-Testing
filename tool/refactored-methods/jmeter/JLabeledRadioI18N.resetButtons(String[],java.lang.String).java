/**
 * Method is responsible for removing current JRadioButtons of ButtonGroup and
 * add creating the JRadioButtons and adding them to
 * the ButtonGroup.
 *
 * The resource name is used as the action command for the button model,
 * and the resource value is used to set the button label.
 *
 * @param resources list of resource names
 * @param selected initially selected resource (if not null)
 */
public void resetButtons(String[] resources, String selected) {
    bGroup.clearSelection();
    for (Enumeration<AbstractButton> buttons = bGroup.getElements(); buttons.hasMoreElements(); ) {
        AbstractButton button = buttons.nextElement();
        button.removeActionListener(this);
        bGroup.remove(button);
        this.remove(button);
    }
    initButtonGroup(resources, selected);
}