public CommunityUserForm(String id, IModel<User> model) {
    super(id, model);
    RadioGroup<Long> rg = new RadioGroup<>("community_settings", new IModel<Long>() {

        private static final long serialVersionUID = 1L;

        @Override
        public Long getObject() {
            User u = CommunityUserForm.this.getModelObject();
            if (u.isShowContactData()) {
                return 1L;
            } else if (u.isShowContactDataToContacts()) {
                return 2L;
            }
            return 3L;
        }

        @Override
        public void setObject(Long choice) {
            User u = CommunityUserForm.this.getModelObject();
            if (choice.equals(1L)) {
                u.setShowContactData(true);
                u.setShowContactDataToContacts(false);
            } else if (choice.equals(2L)) {
                u.setShowContactData(false);
                u.setShowContactDataToContacts(true);
            } else {
                u.setShowContactData(false);
                u.setShowContactDataToContacts(false);
            }
        }
    });
    add(rg.add(new Radio<>("everybody", Model.of(1L)), new Radio<>("contact", Model.of(2L)), new Radio<>("nobody", Model.of(3L))).setOutputMarkupId(true).setRenderBodyOnly(false));
    add(new TextArea<String>("userOffers"));
    add(new TextArea<String>("userSearchs"));
}