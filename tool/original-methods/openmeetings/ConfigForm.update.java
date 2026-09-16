private void update(AjaxRequestTarget target) {
    Configuration c = getModelObject();
    stringBox.setVisible(Type.PATH == c.getType() || Type.STRING == c.getType());
    numberBox.setVisible(Type.NUMBER == c.getType());
    booleanBox.setVisible(Type.BOOL == c.getType());
    hotkeyBox.setVisible(Type.HOTKEY == c.getType());
    if (target != null) {
        target.add(stringBox, numberBox, booleanBox, hotkeyBox);
        if (Type.HOTKEY == c.getType()) {
            target.appendJavaScript("addOmAdminConfigHandlers()");
        }
    }
}