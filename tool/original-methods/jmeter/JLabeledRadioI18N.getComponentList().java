/**
 * Method will return all the label and JRadioButtons. ButtonGroup is
 * excluded from the list.
 */
@Override
@SuppressWarnings("JdkObsolete")
public List<JComponent> getComponentList() {
    List<JComponent> comps = new ArrayList<>();
    comps.add(mLabel);
    Enumeration<AbstractButton> en = this.bGroup.getElements();
    while (en.hasMoreElements()) {
        comps.add(en.nextElement());
    }
    return comps;
}