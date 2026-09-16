@Override
protected void onRefreshSubmit(AjaxRequestTarget target, Form<?> form) {
    LdapConfig ldapConfig = fetchLdapConfig();
    this.setModelObject(ldapConfig);
    target.add(this);
}
// ---- helper method(s) introduced by the refactoring ----
private LdapConfig fetchLdapConfig() {
    LdapConfig ldapConfig = this.getModelObject();
    if (ldapConfig.getId() != null) {
        return ldapDao.get(ldapConfig.getId());
    }
    return new LdapConfig();
}

