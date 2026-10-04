public void init() {
    createInviteDialog();
    createSipDialerDialog();
    createActionsMenu();
    createInviteMenuItem();
    createShareMenuItem();
    createApplyModerMenuItem();
    createApplyWbMenuItem();
    createApplyAvMenuItem();
    createSipDialerMenuItem();
    createDownloadPngMenuItem();
    createDownloadPdfMenuItem();
    createResetWbMenuItem();
    createLocalSettingsMenuItem();
}
// ---- helper method(s) introduced by the refactoring ----
private void createInviteDialog() {
    final String roomInviteDialogId = "roomInviteDialog";
    RoomInvitationForm rif = new RoomInvitationForm("form", room.getRoom().getId(), roomInviteDialogId);
    invite = new InvitationDialog(roomInviteDialogId, rif);
    mp.add(invite);
    rif.setDialog(invite);
}

private void createSipDialerDialog() {
    sipDialer = new SipDialerDialog("sipDialer", room);
    mp.add(sipDialer);
}

private void createActionsMenu() {
    actionsMenu = new OmMenuItem(mp.getString("635"), null, false);
}

private void createInviteMenuItem() {
    inviteMenuItem = new OmMenuItem(mp.getString("213"), mp.getString("1489"), false) {

        private static final long serialVersionUID = 1L;

        @Override
        protected void onClick(AjaxRequestTarget target) {
            invite.updateModel(target);
            invite.show(target);
        }
    };
}

private void createShareMenuItem() {
    shareMenuItem = new OmMenuItem(mp.getString("239"), mp.getString("1480"), false) {

        private static final long serialVersionUID = 1L;

        @Override
        protected void onClick(AjaxRequestTarget target) {
            target.appendJavaScript("Sharer.open();");
        }
    };
}

private void createApplyModerMenuItem() {
    applyModerMenuItem = new OmMenuItem(mp.getString("784"), mp.getString("1481"), false) {

        private static final long serialVersionUID = 1L;

        @Override
        protected void onClick(AjaxRequestTarget target) {
            room.requestRight(Room.Right.MODERATOR, target);
        }
    };
}

private void createApplyWbMenuItem() {
    applyWbMenuItem = new OmMenuItem(mp.getString("785"), mp.getString("1492"), false) {

        private static final long serialVersionUID = 1L;

        @Override
        protected void onClick(AjaxRequestTarget target) {
            room.requestRight(Room.Right.WHITEBOARD, target);
        }
    };
}

private void createApplyAvMenuItem() {
    applyAvMenuItem = new OmMenuItem(mp.getString("786"), mp.getString("1482"), false) {

        private static final long serialVersionUID = 1L;

        @Override
        protected void onClick(AjaxRequestTarget target) {
            room.requestRight(Room.Right.VIDEO, target);
        }
    };
}

private void createSipDialerMenuItem() {
    sipDialerMenuItem = new OmMenuItem(mp.getString("1447"), mp.getString("1488"), false) {

        private static final long serialVersionUID = 1L;

        @Override
        protected void onClick(AjaxRequestTarget target) {
            sipDialer.show(target);
        }
    };
}

private void createDownloadPngMenuItem() {
    downloadPngMenuItem = new OmMenuItem(mp.getString("download.png"), mp.getString("download.png")) {

        private static final long serialVersionUID = 1L;

        @Override
        protected void onClick(AjaxRequestTarget target) {
            download(target, EXTENSION_PNG);
        }
    };
}

private void createDownloadPdfMenuItem() {
    downloadPdfMenuItem = new OmMenuItem(mp.getString("download.pdf"), mp.getString("download.pdf")) {

        private static final long serialVersionUID = 1L;

        @Override
        protected void onClick(AjaxRequestTarget target) {
            download(target, EXTENSION_PDF);
        }
    };
}

private void createResetWbMenuItem() {
    resetWb = new OmMenuItem(mp.getString("reset.whiteboard"), mp.getString("reset.whiteboard")) {

        private static final long serialVersionUID = 1L;

        @Override
        protected void onClick(AjaxRequestTarget target) {
            wbManager.reset(room.getRoom().getId(), getUserId());
        }
    };
}

private void createLocalSettingsMenuItem() {
    localSettings = new OmMenuItem(mp.getString("edit.settings"), mp.getString("edit.settings")) {

        private static final long serialVersionUID = 1L;

        @Override
        protected void updateAjaxAttributes(AjaxRequestAttributes attributes) {
            attributes.getAjaxCallListeners().add(new IAjaxCallListener() {

                @Override
                public CharSequence getPrecondition(Component component) {
                    return "UserSettings.open(); return false;";
                }
            });
        }
    };
}

