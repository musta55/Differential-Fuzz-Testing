@Override
public void onClick(AjaxRequestTarget target, InvitationForm.Action action) {
    if (InvitationForm.Action.SEND == action && Strings.isEmpty(url.getModelObject()) && rdi.getModelObject() == InviteeType.GROUP) {
        sendInvitationsByGroup(target);
    }
    super.onClick(target, action);
}
// ---- helper method(s) introduced by the refactoring ----
private AjaxFormChoiceComponentUpdatingBehavior createInviteeTypeBehavior() {
    return new AjaxFormChoiceComponentUpdatingBehavior() {

        private static final long serialVersionUID = 1L;

        @Override
        protected void onUpdate(AjaxRequestTarget target) {
            boolean groupsEnabled = InviteeType.GROUP == rdi.getModelObject();
            updateButtons(target);
            target.add(groups.setEnabled(groupsEnabled), recipients.setEnabled(!groupsEnabled));
        }
    };
}

private void addComponentsToGroupContainer() {
    groupContainer.add(groups.setRequired(true).add(createGroupsBehavior()).setOutputMarkupId(true), new Radio<>("group", Model.of(InviteeType.GROUP)));
}

private AjaxFormComponentUpdatingBehavior createGroupsBehavior() {
    return AjaxFormComponentUpdatingBehavior.onUpdate(EVT_CHANGE, target -> {
        url.setModelObject(null);
        updateButtons(target);
    });
}

private void addComponentsToRdi(boolean showGroups) {
    rdi.add(recipients, groupContainer.setVisible(showGroups));
    rdi.add(new Radio<>("user", Model.of(InviteeType.USER)));
}

private void addSipContainer() {
    add(sipContainer.setOutputMarkupPlaceholderTag(true).setOutputMarkupId(true));
    sipContainer.add(new Label("room.confno", "").setVisible(false));
}

private void sendInvitationsByGroup(AjaxRequestTarget target) {
    final String userbaseUrl = WebSession.get().getExtendedProperties().getBaseUrl();
    for (Group g : groups.getModelObject()) {
        for (GroupUser ou : groupUserDao.get(g.getId(), 0, Integer.MAX_VALUE)) {
            Invitation i = create(ou.getUser());
            try {
                invitationManager.sendInvitationLink(i, MessageType.CREATE, subject.getModelObject(), message.getModelObject(), false, userbaseUrl);
            } catch (Exception e) {
                log.error("error while sending invitation by Group ", e);
            }
        }
    }
}

