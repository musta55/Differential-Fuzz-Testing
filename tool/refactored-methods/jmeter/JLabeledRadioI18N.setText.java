/**
 * The implementation will iterate through the radio buttons and find the
 * match. It then sets it to selected and sets all other radio buttons as
 * not selected.
 * @param resourceName name of resource whose button is to be selected
 */
@Override
public void setText(String resourceName) {
    for (Enumeration<AbstractButton> en = bGroup.getElements(); en.hasMoreElements(); ) {
        ButtonModel model = en.nextElement().getModel();
        model.setSelected(model.getActionCommand().equals(resourceName));
    }
}