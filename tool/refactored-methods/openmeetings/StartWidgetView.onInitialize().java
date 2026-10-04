@Override
protected void onInitialize() {
    addStepWithBehavior("step1");
    addStepWithBehavior("step2");
    addStepWithAvTest("step3");
    addStepWithBehavior("step4");
    add(//Application here is used to substitute {0}
    new Label("123msg", Application.getString("widget.start.desc")).setEscapeModelStrings(false));
    add(new BootstrapButton("start", new ResourceModel("773"), Buttons.Type.Outline_Primary).add(new PublicRoomsEventBehavior()));
    add(new BootstrapButton("calendar", new ResourceModel("291"), Buttons.Type.Outline_Primary).add(new AjaxEventBehavior(EVT_CLICK) {

        private static final long serialVersionUID = 1L;

        @Override
        protected void onEvent(AjaxRequestTarget target) {
            ((MainPage) getPage()).updateContents(CALENDAR, target);
        }
    }));
    super.onInitialize();
}
// ---- helper method(s) introduced by the refactoring ----
private void addStepWithBehavior(String id) {
    add(new WebMarkupContainer(id).add(new PublicRoomsEventBehavior()));
}

private void addStepWithAvTest(String id) {
    add(new WebMarkupContainer(id).add(new WebMarkupContainer("avTest").add(AttributeModifier.append("href", RequestCycle.get().urlFor(HashPage.class, new PageParameters().add(APP, APP_TYPE_SETTINGS)).toString()))));
}

