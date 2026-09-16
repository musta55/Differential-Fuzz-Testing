@Override
protected void onInitialize() {
    header(new ResourceModel("1209"));
    setUseCloseHandler(true);
    size(Modal.Size.Large);
    addButton(new BootstrapAjaxButton(BUTTON_MARKUP_ID, new ResourceModel("218"), form, Buttons.Type.Outline_Primary) {

        private static final long serialVersionUID = 1L;

        @Override
        protected void onError(AjaxRequestTarget target) {
            target.add(feedback);
        }

        @Override
        protected void onSubmit(AjaxRequestTarget target) {
            PrivateMessage m = MessageDialog.this.getModelObject();
            m.setInserted(new Date());
            User owner = userDao.get(getUserId());
            handleRoomBooking(m, owner);
            sendMessages(m, owner);
            MessageDialog.this.close(target);
            onSend(target);
        }
    });
    // send
    addButton(OmModalCloseButton.of());
    form.add(feedback.setOutputMarkupId(true));
    final UserMultiChoice recepients = new UserMultiChoice("to", modelTo);
    recepients.getSettings().setDropdownParent(MessageDialog.this.getMarkupId());
    form.add(recepients.setRequired(true));
    form.add(new TextField<String>("subject"));
    OmWysiwygToolbar toolbar = new OmWysiwygToolbar("toolbarContainer");
    form.add(toolbar);
    form.add(new WysiwygEditor("message", toolbar));
    form.add(roomParamsBlock.setOutputMarkupId(true));
    final CheckBox bookedRoom = new CheckBox("bookedRoom");
    form.add(bookedRoom.setOutputMarkupId(true).add(new AjaxEventBehavior(EVT_CLICK) {

        private static final long serialVersionUID = 1L;

        @Override
        protected void onEvent(AjaxRequestTarget target) {
            PrivateMessage p = MessageDialog.this.getModelObject();
            p.setBookedRoom(!p.isBookedRoom());
            roomParams.setVisible(p.isBookedRoom());
            target.add(bookedRoom, roomParamsBlock);
        }
    }));
    bookedRoom.setVisible(isMyRoomsEnabled());
    roomParamsBlock.add(roomParams);
    roomParams.add(new RoomTypeDropDown("room.type"));
    roomParams.add(start);
    roomParams.add(end);
    add(form.setOutputMarkupId(true));
    super.onInitialize();
}
// ---- helper method(s) introduced by the refactoring ----
private void handleRoomBooking(PrivateMessage m, User owner) {
    if (m.isBookedRoom()) {
        Room r = m.getRoom();
        r.setName(m.getSubject());
        r.setComment("");
        r.setCapacity(100L);
        r.setAppointment(true);
        r.setAllowUserQuestions(true);
        r = roomDao.update(r, getUserId());
        Appointment a = new Appointment();
        a.setTitle(m.getSubject());
        a.setDescription(m.getMessage());
        a.setRoom(r);
        a.setStart(CalendarWebHelper.getDate(start.getModelObject()));
        a.setEnd(CalendarWebHelper.getDate(end.getModelObject()));
        List<MeetingMember> attendees = new ArrayList<>();
        for (User to : modelTo.getObject()) {
            MeetingMember mm = new MeetingMember();
            mm.setUser(to);
            mm.setDeleted(false);
            mm.setAppointment(a);
            attendees.add(mm);
        }
        a.setOwner(owner);
        a.setMeetingMembers(attendees);
        apptDao.update(a, getUserId(), false);
        m.setRoom(r);
    } else {
        m.setRoom(null);
    }
}

private void sendMessages(PrivateMessage m, User owner) {
    for (User to : modelTo.getObject()) {
        if (to.getId() == null) {
            userDao.update(to, getUserId());
        }
        //to send
        PrivateMessage p = new PrivateMessage(m);
        p.setTo(to);
        p.setFolderId(SENT_FOLDER_ID);
        msgDao.update(p, getUserId());
        //to inbox
        p = new PrivateMessage(m);
        p.setOwner(to);
        p.setFolderId(INBOX_FOLDER_ID);
        msgDao.update(p, getUserId());
        if (to.getAddress() != null) {
            String aLinkHTML = (isPrivate && to.getType() == Type.USER) ? "<br/><br/>" + "<a href='" + getContactsLink() + "'>" + Application.getString("1302", to.getLanguageId()) + "</a><br/>" : "";
            String invitationLink = generateInvitationLink(p, owner, to);
            String subj = p.getSubject() == null ? "" : p.getSubject();
            handler.send(to.getAddress().getEmail(), Application.getString("1301", to.getLanguageId()) + subj, (p.getMessage() == null ? "" : p.getMessage().replaceAll("\\<[^>]*+>", "")) + aLinkHTML + invitationLink);
        }
    }
}

private String generateInvitationLink(PrivateMessage p, User owner, User to) {
    String invitationLink = "";
    if (p.isBookedRoom()) {
        Invitation i = inviteManager.getInvitation(to, p.getRoom(), false, null, Valid.PERIOD, owner, to.getLanguageId(), CalendarHelper.getDate(start.getModelObject(), to.getTimeZoneId()), CalendarHelper.getDate(end.getModelObject(), to.getTimeZoneId()), null);
        invitationLink = getInvitationLink(i, WebSession.get().getExtendedProperties().getBaseUrl());
        if (invitationLink != null) {
            invitationLink = //
            "<br/>" + Application.getString("template.room.invitation.text", to.getLanguageId()) + "<br/><a href='" + invitationLink + "'>" + Application.getString("504", to.getLanguageId()) + "</a><br/>";
        }
    }
    return invitationLink;
}

