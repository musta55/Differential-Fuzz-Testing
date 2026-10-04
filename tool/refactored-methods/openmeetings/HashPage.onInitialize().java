@Override
protected void onInitialize() {
    super.onInitialize();
    WebSession ws = WebSession.get();
    String errorMsg = getString("invalid.hash");
    recContainer.setVisible(false);
    add(new EmptyPanel(PANEL_MAIN).setVisible(false));
    if (!invitation.isEmpty()) {
        handleInvitation(ws, errorMsg);
    } else if (!secure.isEmpty()) {
        handleSecure(ws, errorMsg);
    }
    if (!app.isEmpty()) {
        handleAppType();
    }
    add(recContainer.add(videoInfo.setOutputMarkupPlaceholderTag(true), videoPlayer.setOutputMarkupPlaceholderTag(true)), new InvitationPasswordDialog("i-pass", this));
    remove(urlParametersReceivingBehavior);
    add(new IconTextModal("access-denied").withLabel(errorMsg).withErrorIcon().addButton(OmModalCloseButton.of("54")).header(new ResourceModel("invalid.hash")).show(error));
}
// ---- helper method(s) introduced by the refactoring ----
private void handleInvitation(WebSession ws, String errorMsg) {
    Invitation i = ws.getInvitation();
    if (i == null) {
        errorMsg = getString("error.hash.invalid");
    } else if (!i.isAllowEntry()) {
        FastDateFormat sdf = FormatHelper.getDateTimeFormat(i.getInvitee());
        errorMsg = Valid.ONE_TIME == i.getValid() ? getString("error.hash.used") : String.format("%s %s - %s, %s", getString("error.hash.period"), sdf.format(i.getValidFrom()), sdf.format(i.getValidTo()), i.getInvitee().getTimeZoneId());
    } else {
        handleInvitationRecording(i);
        handleInvitationRoom(i);
    }
}

private void handleInvitationRecording(Invitation i) {
    Recording rec = i.getRecording();
    if (rec != null) {
        videoInfo.setVisible(!i.isPasswordProtected());
        videoPlayer.setVisible(!i.isPasswordProtected());
        if (!i.isPasswordProtected()) {
            videoInfo.update(null, rec);
            videoPlayer.update(null, rec);
        }
        recContainer.setVisible(true);
        error = false;
    }
}

private void handleInvitationRoom(Invitation i) {
    Room r = i.getRoom();
    if (r != null && !r.isDeleted()) {
        createRoom(r.getId());
        if (i.isPasswordProtected() && roomPanel != null) {
            mainPanel.getChat().setVisible(false);
            roomPanel.setOutputMarkupPlaceholderTag(true).setVisible(false);
        }
    }
}

private void handleSecure(WebSession ws, String errorMsg) {
    Long recId = getRecordingId(), roomId = ws.getRoomId();
    if (recId == null && roomId == null) {
        errorMsg = getString("1599");
    } else if (recId != null) {
        recContainer.setVisible(true);
        Recording rec = recDao.get(recId);
        videoInfo.update(null, rec);
        videoPlayer.update(null, rec);
        error = false;
    } else {
        createRoom(roomId);
    }
}

private void handleAppType() {
    if (APP_TYPE_NETWORK.equals(app.toString())) {
        replace(new NetTestPanel(PANEL_MAIN).add(AttributeModifier.append("class", "app")));
        error = false;
    }
    if (APP_TYPE_SETTINGS.equals(app.toString())) {
        replace(new VideoSettings(PANEL_MAIN).replace(new OmWebSocketPanel("ws-panel") {

            private static final long serialVersionUID = 1L;

            private WsClient c = null;

            @Override
            protected void onConnect(ConnectedMessage message) {
                c = new WsClient(message.getSessionId(), message.getKey().hashCode());
            }

            @Override
            protected void onConnect(WebSocketRequestHandler handler) {
                super.onConnect(handler);
                handler.appendJavaScript(String.format("VideoSettings.init(%s);VideoSettings.open();", VideoSettings.getInitJson("noclient").put("infoMsg", getString("close.settings.tab"))));
            }

            @Override
            protected IWsClient getWsClient() {
                return c;
            }
        }).add(new OmAjaxClientInfoBehavior()));
        error = false;
    }
}

