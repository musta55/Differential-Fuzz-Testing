@Override
protected void onInitialize() {
    User u = (User) getDefaultModelObject();
    infoPanel.add(new ProfileImagePanel("img", u.getId()));
    infoPanel.add(new Label("firstname"));
    infoPanel.add(new Label("lastname"));
    infoPanel.add(new Label("timeZoneId"));
    infoPanel.add(new Label("regdate"));
    infoPanel.add(new TextArea<String>("userOffers").setEnabled(false));
    infoPanel.add(new TextArea<String>("userSearchs").setEnabled(false));
    if (getUserId().equals(u.getId()) || u.isShowContactData() || (u.isShowContactDataToContacts() && contactDao.isContact(u.getId(), getUserId()))) {
        addressDenied.setVisible(false);
        address.add(new Label("address.phone"));
        address.add(new Label("address.street"));
        address.add(new Label("address.additionalname"));
        address.add(new Label("address.zip"));
        address.add(new Label("address.town"));
        address.add(new Label("country", getCountryName(u.getAddress().getCountry(), getLocale())));
        address.add(new Label("address.comment"));
    } else {
        address.setVisible(false);
        addressDenied.setDefaultModelObject(getString(u.isShowContactDataToContacts() ? "1269" : "1268"));
    }
    infoPanel.add(address.setDefaultModel(getDefaultModel()));
    infoPanel.add(addressDenied);
    add(infoPanel.setOutputMarkupId(true));
    super.onInitialize();
}