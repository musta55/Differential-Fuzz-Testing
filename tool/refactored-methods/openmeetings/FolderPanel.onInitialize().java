@Override
protected void onInitialize() {
    super.onInitialize();
    final BaseFileItem f = (BaseFileItem) getDefaultModelObject();
    boolean editable = treePanel.isEditable() && !f.isReadOnly();
    addDroppableBehaviorIfFolderAndEditable(f, editable);
    addDraggableBehaviorIfEditable(f);
    addNameComponent(f, editable);
    addTitleAttribute(f);
    addStyleClassBehavior();
    addClickBehavior(f);
}
// ---- helper method(s) introduced by the refactoring ----
private void addDroppableBehaviorIfFolderAndEditable(BaseFileItem f, boolean editable) {
    if (f.getType() == Type.FOLDER && editable) {
        add(new DroppableBehavior(JQueryWidget.getSelector(this), new Options().set("hoverClass", Options.asString("bg-light")).set("accept", Options.asString(f instanceof Recording ? ".recorditem" : ".fileitem")), this));
    }
}

private void addDraggableBehaviorIfEditable(BaseFileItem f) {
    if (f.getId() != null && treePanel.isEditable()) {
        add(new DraggableBehavior(JQueryWidget.getSelector(this), new Options().set("revert", "OmFileTree.treeRevert").set("cursor", Options.asString("move")).set("helper", "OmFileTree.dragHelper").set("cursorAt", "{left: 40, top: 18}").set("containment", Options.asString(treePanel.getContainment())), this));
    }
}

private void addNameComponent(BaseFileItem f, boolean editable) {
    Component name = f.getId() == null || !editable ? new Label("name", f.getName()) : createAjaxEditableLabel(f);
    add(name);
}

private AjaxEditableLabel<String> createAjaxEditableLabel(BaseFileItem f) {
    return new AjaxEditableLabel<>("name", Model.of(f.getName())) {

        private static final long serialVersionUID = 1L;

        @Override
        protected String getLabelAjaxEvent() {
            return "dblclick";
        }

        @Override
        protected void onSubmit(AjaxRequestTarget target) {
            super.onSubmit(target);
            f.setName(getEditor().getModelObject());
            updateFileOrRecording(f);
        }
    };
}

private void updateFileOrRecording(BaseFileItem f) {
    if (f instanceof Recording rec) {
        recDao.update(rec);
    } else {
        fileDao.update((FileItem) f);
    }
}

private void addTitleAttribute(BaseFileItem f) {
    add(AttributeModifier.append(ATTR_TITLE, f.getName()));
}

private void addStyleClassBehavior() {
    add(styleClass);
}

private void addClickBehavior(BaseFileItem f) {
    add(new AjaxEventBehavior("click") {

        private static final long serialVersionUID = 1L;

        @Override
        public void onEvent(AjaxRequestTarget target) {
            onClick(target, f);
        }

        @Override
        protected void updateAjaxAttributes(AjaxRequestAttributes attributes) {
            super.updateAjaxAttributes(attributes);
            attributes.getDynamicExtraParameters().add(String.format("return {%s: JSON.stringify({%s: attrs.event.shiftKey, %s: attrs.event.ctrlKey})};", PARAM_MOD, PARAM_SHIFT, PARAM_CTRL));
        }
    });
}

private void toggleOrExpandFolder(BaseFileItem f, AjaxRequestTarget target) {
    if (Type.FOLDER == f.getType() && treePanel.tree.getState(f) == State.COLLAPSED) {
        treePanel.tree.expand(f);
    } else {
        treePanel.update(target, f);
    }
}

