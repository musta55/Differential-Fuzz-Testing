public WelcomeWidgetView(String id, Model<Widget> model) {
    super(id, model);
    User user = retrieveUser();
    addUserProfileComponents(user);
    addOpenUnreadLink(user);
    addEditProfileLink();
    addNetworkTestLink();
    addAvTestLink();
}
// ---- helper method(s) introduced by the refactoring ----
private User retrieveUser() {
    return userDao.get(getUserId());
}

private void addUserProfileComponents(User user) {
    add(new UploadableProfileImagePanel("img", getUserId()));
    add(new Label("firstname", Model.of(user.getFirstname())));
    add(new Label("lastname", Model.of(user.getLastname())));
    add(new Label("tz", Model.of(user.getTimeZoneId())));
}

private void addOpenUnreadLink(User user) {
    AjaxLink<Void> openUnreadLink = new AjaxLink<Void>("openUnread") {

        private static final long serialVersionUID = 1L;

        @Override
        public void onClick(AjaxRequestTarget target) {
            ((MainPage) getPage()).updateContents(PROFILE_MESSAGES, target);
        }
    };
    openUnreadLink.add(new Label("unread", Model.of(String.valueOf(msgDao.count(getUserId(), INBOX_FOLDER_ID, null)))));
    add(openUnreadLink);
}

private void addEditProfileLink() {
    AjaxLink<Void> editProfileLink = new AjaxLink<Void>("editProfile") {

        private static final long serialVersionUID = 1L;

        @Override
        public void onClick(AjaxRequestTarget target) {
            ((MainPage) getPage()).updateContents(PROFILE_EDIT, target);
        }
    };
    add(editProfileLink);
}

private void addNetworkTestLink() {
    add(new WebMarkupContainer("netTest").add(AttributeModifier.append("href", RequestCycle.get().urlFor(HashPage.class, new PageParameters().add(APP, APP_TYPE_NETWORK)).toString())));
}

private void addAvTestLink() {
    add(new WebMarkupContainer("avTest").add(AttributeModifier.append("href", RequestCycle.get().urlFor(HashPage.class, new PageParameters().add(APP, APP_TYPE_SETTINGS)).toString())));
}

