@Override
protected void onInitialize() {
    add(new WebMarkupContainer("step1").add(new PublicRoomsEventBehavior()));
    add(new WebMarkupContainer("step2").add(new PublicRoomsEventBehavior()));
    add(new WebMarkupContainer("step3").add(new WebMarkupContainer("avTest").add(AttributeModifier.append("href", RequestCycle.get().urlFor(HashPage.class, new PageParameters().add(APP, APP_TYPE_SETTINGS)).toString()))));
    add(new WebMarkupContainer("step4").add(new PublicRoomsEventBehavior()));
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