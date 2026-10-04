@Override
protected void onInitialize() {
    super.onInitialize();
    final BaseFileItem f = (BaseFileItem) getDefaultModelObject();
    boolean editable = treePanel.isEditable() && !f.isReadOnly();
    final String selector = JQueryWidget.getSelector(this);
    if (f.getType() == Type.FOLDER && editable) {
        add(new DroppableBehavior(selector, new Options().set("hoverClass", Options.asString("bg-light")).set("accept", Options.asString(getDefaultModelObject() instanceof Recording ? ".recorditem" : ".fileitem")), this));
    }
    if (f.getId() != null && treePanel.isEditable()) {
        add(new DraggableBehavior(selector, new Options().set("revert", "OmFileTree.treeRevert").set("cursor", Options.asString("move")).set("helper", "OmFileTree.dragHelper").set("cursorAt", "{left: 40, top: 18}").set("containment", Options.asString(treePanel.getContainment())), this));
    }
    Component name = f.getId() == null || !editable ? new Label("name", f.getName()) : new AjaxEditableLabel<>("name", Model.of(f.getName())) {

        private static final long serialVersionUID = 1L;

        @Override
        protected String getLabelAjaxEvent() {
            return "dblclick";
        }

        @Override
        protected void onSubmit(AjaxRequestTarget target) {
            super.onSubmit(target);
            f.setName(getEditor().getModelObject());
            if (f instanceof Recording rec) {
                recDao.update(rec);
            } else {
                fileDao.update((FileItem) f);
            }
        }
    };
    add(name);
    add(AttributeModifier.append(ATTR_TITLE, f.getName()));
    add(styleClass);
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