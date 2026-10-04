public CommunityUserForm(String id, IModel<User> model) {
    super(id, model);
    RadioGroup<Long> rg = new RadioGroup<>("community_settings", createRadioGroupModel());
    add(rg.add(createRadio("everybody", EVERYBODY), createRadio("contact", CONTACT), createRadio("nobody", NOBODY)).setOutputMarkupId(true).setRenderBodyOnly(false));
    add(new TextArea<String>("userOffers"));
    add(new TextArea<String>("userSearchs"));
}
// ---- helper method(s) introduced by the refactoring ----
private IModel<Long> createRadioGroupModel() {
    return new IModel<Long>() {

        private static final long serialVersionUID = 1L;

        @Override
        public Long getObject() {
            return getRadioChoiceFromUser();
        }

        @Override
        public void setObject(Long choice) {
            setUserFromRadioChoice(choice);
        }
    };
}

private Radio<Long> createRadio(String id, Long value) {
    return new Radio<>(id, Model.of(value));
}

private Long getRadioChoiceFromUser() {
    User u = getModelObject();
    if (u.isShowContactData()) {
        return EVERYBODY;
    } else if (u.isShowContactDataToContacts()) {
        return CONTACT;
    }
    return NOBODY;
}

private void setUserFromRadioChoice(Long choice) {
    User u = getModelObject();
    if (choice.equals(EVERYBODY)) {
        u.setShowContactData(true);
        u.setShowContactDataToContacts(false);
    } else if (choice.equals(CONTACT)) {
        u.setShowContactData(false);
        u.setShowContactDataToContacts(true);
    } else {
        u.setShowContactData(false);
        u.setShowContactDataToContacts(false);
    }
}

