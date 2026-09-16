public void updateModelObject(User u, boolean isAdminForm) {
    clearAndAddGroupUsers(u);
    if (isAdminForm) {
        addAdminGroups(u);
    }
}
// ---- helper method(s) introduced by the refactoring ----
private void addEmailField() {
    add(email);
    email.setLabel(new ResourceModel("119"));
    email.add(RfcCompliantEmailAddressValidator.getInstance());
}

private void addSalutationField() {
    add(new DropDownChoice<>("salutation", List.of(Salutation.values()), new LambdaChoiceRenderer<>(s -> getString("user.salutation." + s.name()), Salutation::name)));
}

private void addNameFields() {
    add(new TextField<String>("firstname"));
    add(new TextField<String>("lastname"));
}

private void addDisplayNameField() {
    add(new TextField<String>("displayName").setEnabled(isAdminForm || isDisplayNameEditable()));
}

private void addTimeZoneField() {
    add(new DropDownChoice<>("timeZoneId", AVAILABLE_TIMEZONES));
}

private void addLanguageField() {
    add(new LanguageDropDown("languageId"));
}

private void addPhoneField() {
    add(new TextField<String>("address.phone"));
}

private void addBirthdayField() {
    add(bday);
}

private void addAddressFields() {
    add(new TextField<String>("address.street"));
    add(new TextField<String>("address.additionalname"));
    add(new TextField<String>("address.zip"));
    add(new TextField<String>("address.town"));
    add(new CountryDropDown("address.country"));
    add(new TextArea<String>("address.comment"));
}

private void addGroupUsersField() {
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

private void clearAndAddGroupUsers(User u) {
    grpUsers.clear();
    grpUsers.addAll(u.getGroupUsers());
}

private void addAdminGroups(User u) {
    List<Group> grpList = hasGroupAdminLevel(getRights()) ? groupDao.adminGet(null, getUserId(), 0, Integer.MAX_VALUE, null) : groupDao.get(0, Integer.MAX_VALUE);
    for (Group g : grpList) {
        GroupUser gu = new GroupUser(g, u);
        int idx = grpUsers.indexOf(gu);
        if (idx < 0) {
            grpUsers.add(gu);
        }
    }
}

