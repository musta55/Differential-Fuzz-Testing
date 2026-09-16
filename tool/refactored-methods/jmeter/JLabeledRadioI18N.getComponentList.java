/**
 * Method will return all the label and JRadioButtons. ButtonGroup is
 * excluded from the list.
 */
@Override
public List<JComponent> getComponentList() {
    List<JComponent> comps = new ArrayList<>();
    comps.add(mLabel);
    Collections.list(bGroup.getElements()).forEach(comps::add);
    return comps;
}