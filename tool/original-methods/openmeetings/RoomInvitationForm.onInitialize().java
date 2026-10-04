@Override
protected void onInitialize() {
    groups.setLabel(new ResourceModel("126"));
    boolean showGroups = AuthLevelUtil.hasAdminLevel(getRights()) || AuthLevelUtil.hasGroupAdminLevel(getRights());
    add(rdi.add(new AjaxFormChoiceComponentUpdatingBehavior() {

        private static final long serialVersionUID = 1L;

        @Override
        protected void onUpdate(AjaxRequestTarget target) {
            boolean groupsEnabled = InviteeType.GROUP == rdi.getModelObject();
            updateButtons(target);
            target.add(groups.setEnabled(groupsEnabled), recipients.setEnabled(!groupsEnabled));
        }
    }));
    groupContainer.add(groups.setRequired(true).add(AjaxFormComponentUpdatingBehavior.onUpdate(EVT_CHANGE, target -> {
        url.setModelObject(null);
        updateButtons(target);
    })).setOutputMarkupId(true), new Radio<>("group", Model.of(InviteeType.GROUP)));
    rdi.add(recipients, groupContainer.setVisible(showGroups));
    rdi.add(new Radio<>("user", Model.of(InviteeType.USER)));
    add(sipContainer.setOutputMarkupPlaceholderTag(true).setOutputMarkupId(true));
    sipContainer.add(new Label("room.confno", "")).setVisible(false);
    if (dropDownParentId != null) {
        groups.getSettings().setDropdownParent(dropDownParentId);
    }
    super.onInitialize();
}