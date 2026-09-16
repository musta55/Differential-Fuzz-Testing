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
@SuppressWarnings("JdkObsolete")
public void resetButtons(String[] resources, String selected) {
    Enumeration<AbstractButton> buttons = bGroup.getElements();
    List<AbstractButton> buttonsToRemove = new ArrayList<>(this.bGroup.getButtonCount());
    while (buttons.hasMoreElements()) {
        AbstractButton abstractButton = buttons.nextElement();
        buttonsToRemove.add(abstractButton);
    }
    for (AbstractButton abstractButton : buttonsToRemove) {
        abstractButton.removeActionListener(this);
        bGroup.remove(abstractButton);
    }
    for (AbstractButton abstractButton : buttonsToRemove) {
        this.remove(abstractButton);
    }
    initButtonGroup(resources, selected);
}