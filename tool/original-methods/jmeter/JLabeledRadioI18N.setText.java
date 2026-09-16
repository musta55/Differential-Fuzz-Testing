/**
 * The implementation will iterate through the radio buttons and find the
 * match. It then sets it to selected and sets all other radio buttons as
 * not selected.
 * @param resourceName name of resource whose button is to be selected
 */
@Override
@SuppressWarnings("JdkObsolete")
public void setText(String resourceName) {
    Enumeration<AbstractButton> en = this.bGroup.getElements();
    while (en.hasMoreElements()) {
        ButtonModel model = en.nextElement().getModel();
        if (model.getActionCommand().equals(resourceName)) {
            this.bGroup.setSelected(model, true);
        } else {
            this.bGroup.setSelected(model, false);
        }
    }
}