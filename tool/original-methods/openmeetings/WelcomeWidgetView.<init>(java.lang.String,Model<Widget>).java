public WelcomeWidgetView(String id, Model<Widget> model) {
    super(id, model);
    User u = userDao.get(getUserId());
    add(new UploadableProfileImagePanel("img", getUserId()));
    add(new Label("firstname", Model.of(u.getFirstname())));
    add(new Label("lastname", Model.of(u.getLastname())));
    add(new Label("tz", Model.of(u.getTimeZoneId())));
    add(new AjaxLink<Void>("openUnread") {

        private static final long serialVersionUID = 1L;

        @Override
        public void onClick(AjaxRequestTarget target) {
            ((MainPage) getPage()).updateContents(PROFILE_MESSAGES, target);
        }
    }.add(new Label("unread", Model.of(String.valueOf(msgDao.count(getUserId(), INBOX_FOLDER_ID, null))))));
    add(new AjaxLink<Void>("editProfile") {

        private static final long serialVersionUID = 1L;

        @Override
        public void onClick(AjaxRequestTarget target) {
            ((MainPage) getPage()).updateContents(PROFILE_EDIT, target);
        }
    });
    add(new WebMarkupContainer("netTest").add(AttributeModifier.append("href", RequestCycle.get().urlFor(HashPage.class, new PageParameters().add(APP, APP_TYPE_NETWORK)).toString())));
    add(new WebMarkupContainer("avTest").add(AttributeModifier.append("href", RequestCycle.get().urlFor(HashPage.class, new PageParameters().add(APP, APP_TYPE_SETTINGS)).toString())));
}