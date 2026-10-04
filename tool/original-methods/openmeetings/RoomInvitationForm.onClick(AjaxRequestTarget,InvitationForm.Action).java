@Override
public void onClick(AjaxRequestTarget target, InvitationForm.Action action) {
    if (InvitationForm.Action.SEND == action && Strings.isEmpty(url.getModelObject()) && rdi.getModelObject() == InviteeType.GROUP) {
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
    super.onClick(target, action);
}