public void onClick(AjaxRequestTarget target, Action action) {
    final String userbaseUrl = WebSession.get().getExtendedProperties().getBaseUrl();
    if (Action.GENERATE == action) {
        Invitation i = create(recipients.getModelObject().iterator().next());
        setModelObject(i);
        url.setModelObject(getInvitationLink(i, userbaseUrl));
        target.add(url);
    } else {
        if (Strings.isEmpty(url.getModelObject())) {
            for (User u : recipients.getModelObject()) {
                Invitation i = create(u);
                try {
                    inviteManager.sendInvitationLink(i, MessageType.CREATE, subject.getModelObject(), message.getModelObject(), false, userbaseUrl);
                } catch (Exception e) {
                    log.error("error while sending invitation by User ", e);
                }
            }
        } else {
            Invitation i = getModelObject();
            try {
                inviteManager.sendInvitationLink(i, MessageType.CREATE, subject.getModelObject(), message.getModelObject(), false, userbaseUrl);
            } catch (Exception e) {
                log.error("error while sending invitation by URL ", e);
            }
        }
        dialog.close(target);
    }
}