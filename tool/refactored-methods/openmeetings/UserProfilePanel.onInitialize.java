@Override
protected void onInitialize() {
    User u = (User) getDefaultModelObject();
    addInfoPanelComponents(u);
    addAddressComponents(u);
    add(infoPanel.setOutputMarkupId(true));
    super.onInitialize();
}
// ---- helper method(s) introduced by the refactoring ----
private void addInfoPanelComponents(User u) {
    infoPanel.add(new ProfileImagePanel("img", u.getId()));
    infoPanel.add(new Label("firstname"));
    infoPanel.add(new Label("lastname"));
    infoPanel.add(new Label("timeZoneId"));
    infoPanel.add(new Label("regdate"));
    infoPanel.add(new TextArea<String>("userOffers").setEnabled(false));
    infoPanel.add(new TextArea<String>("userSearchs").setEnabled(false));
    infoPanel.add(address.setDefaultModel(getDefaultModel()));
    infoPanel.add(addressDenied);
}

private void addAddressComponents(User u) {
    if (canShowAddress(u)) {
        addressDenied.setVisible(false);
        addAddressLabels();
    } else {
        address.setVisible(false);
        addressDenied.setDefaultModelObject(getString(u.isShowContactDataToContacts() ? "1269" : "1268"));
    }
}

private boolean canShowAddress(User u) {
    return getUserId().equals(u.getId()) || u.isShowContactData() || (u.isShowContactDataToContacts() && contactDao.isContact(u.getId(), getUserId()));
}

private void addAddressLabels() {
    address.add(new Label("address.phone"));
    address.add(new Label("address.street"));
    address.add(new Label("address.additionalname"));
    address.add(new Label("address.zip"));
    address.add(new Label("address.town"));
    address.add(new Label("country", getCountryName(((User) getDefaultModelObject()).getAddress().getCountry(), getLocale())));
    address.add(new Label("address.comment"));
}

