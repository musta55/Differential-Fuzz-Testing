@Override
protected void onInitialize() {
    super.onInitialize();
    add(email);
    email.setLabel(new ResourceModel("119"));
    email.add(RfcCompliantEmailAddressValidator.getInstance());
    add(new DropDownChoice<>("salutation", List.of(Salutation.values()), new LambdaChoiceRenderer<>(s -> getString("user.salutation." + s.name()), Salutation::name)));
    add(new TextField<String>("firstname"));
    add(new TextField<String>("lastname"));
    add(new TextField<String>("displayName").setEnabled(isAdminForm || isDisplayNameEditable()));
    add(new DropDownChoice<>("timeZoneId", AVAILABLE_TIMEZONES));
    add(new LanguageDropDown("languageId"));
    add(new TextField<String>("address.phone"));
    add(bday);
    add(new TextField<String>("address.street"));
    add(new TextField<String>("address.additionalname"));
    add(new TextField<String>("address.zip"));
    add(new TextField<String>("address.town"));
    add(new CountryDropDown("address.country"));
    add(new TextArea<String>("address.comment"));
    add(new Select2MultiChoice<>("groupUsers", null, new RestrictiveChoiceProvider<GroupUser>() {

        private static final long serialVersionUID = 1L;

        @Override
        public String getDisplayValue(GroupUser choice) {
            return choice.getGroup().getName();
        }

        @Override
        public String toId(GroupUser choice) {
            Long id = choice.getGroup().getId();
            return id == null ? null : "" + id;
        }

        @Override
        public void query(String term, int page, Response<GroupUser> response) {
            for (GroupUser ou : grpUsers) {
                if (Strings.isEmpty(term) || ou.getGroup().getName().contains(term)) {
                    response.add(ou);
                }
            }
        }

        @Override
        public GroupUser fromId(String inId) {
            Long id = Long.parseLong(inId);
            User u = GeneralUserForm.this.getModelObject();
            Group g = groupDao.get(id);
            GroupUser gu = new GroupUser(g, u);
            int idx = grpUsers.indexOf(gu);
            return idx < 0 ? gu : grpUsers.get(idx);
        }
    }).setLabel(new ResourceModel("161")).setRequired(isAdminForm && hasGroupAdminLevel(getRights())).setEnabled(isAdminForm));
}