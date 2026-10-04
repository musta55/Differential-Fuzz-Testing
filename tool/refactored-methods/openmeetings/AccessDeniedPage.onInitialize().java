@Override
protected void onInitialize() {
    super.onInitialize();
    Form<Void> form = new Form<>("form");
    form.add(createHomeButton());
    form.add(createLogoutButton());
    add(form);
}
// ---- helper method(s) introduced by the refactoring ----
private BootstrapButton createHomeButton() {
    return new BootstrapButton("home", new ResourceModel("124"), Buttons.Type.Outline_Primary) {

        private static final long serialVersionUID = 1L;

        @Override
        public void onSubmit() {
            setResponsePage(Application.get().getHomePage());
        }
    };
}

private BootstrapButton createLogoutButton() {
    return new BootstrapButton("logout", new ResourceModel("310"), Buttons.Type.Outline_Danger) {

        private static final long serialVersionUID = 1L;

        @Override
        public boolean isVisible() {
            return WebSession.get().isSignedIn();
        }

        @Override
        public void onSubmit() {
            getSession().invalidate();
            setResponsePage(Application.get().getSignInPageClass());
        }
    };
}

