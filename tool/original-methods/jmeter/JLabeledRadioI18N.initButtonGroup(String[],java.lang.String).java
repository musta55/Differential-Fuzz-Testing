/**
 * Method is responsible for creating the JRadioButtons and adding them to
 * the ButtonGroup.
 *
 * The resource name is used as the action command for the button model,
 * and the resource value is used to set the button label.
 *
 * @param resources list of resource names
 * @param selected initially selected resource (if not null)
 */
private void initButtonGroup(String[] resources, String selected) {
    for (String resource : resources) {
        JRadioButton btn = new JRadioButton(JMeterUtils.getResString(resource));
        btn.setActionCommand(resource);
        btn.addActionListener(this);
        // add the button to the button group
        this.bGroup.add(btn);
        // add the button
        this.add(btn);
        if (selected != null && selected.equals(resource)) {
            btn.setSelected(true);
        }
    }
}