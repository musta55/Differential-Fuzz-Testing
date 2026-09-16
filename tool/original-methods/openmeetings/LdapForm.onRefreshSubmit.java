@Override
protected void onRefreshSubmit(AjaxRequestTarget target, Form<?> form) {
    LdapConfig ldapConfig = this.getModelObject();
    if (ldapConfig.getId() != null) {
        ldapConfig = ldapDao.get(ldapConfig.getId());
    } else {
        ldapConfig = new LdapConfig();
    }
    this.setModelObject(ldapConfig);
    target.add(this);
}