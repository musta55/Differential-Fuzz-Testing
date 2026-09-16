@Override
protected void onInitialize() {
    header(new ResourceModel("18"));
    setCloseOnEscapeKey(false);
    setBackdrop(Backdrop.STATIC);
    add(form = new PollAnswerForm("form", new CompoundPropertyModel<>(new RoomPollAnswer())));
    addButton(new BootstrapAjaxButton(BUTTON_MARKUP_ID, new ResourceModel("32"), form, Buttons.Type.Outline_Primary) {

        private static final long serialVersionUID = 1L;

        @Override
        protected void onSubmit(AjaxRequestTarget target) {
            RoomPollAnswer a = form.getModelObject();
            Long roomId = a.getRoomPoll().getRoom().getId();
            if (pollDao.notVoted(roomId, getUserId())) {
                a.setVoteDate(new Date());
                a.getRoomPoll().getAnswers().add(a);
                pollDao.update(a.getRoomPoll());
            }
            sendRoom(new RoomMessage(roomId, findParent(MainPanel.class).getClient(), RoomMessage.Type.POLL_UPDATED));
            close(target);
        }

        @Override
        protected void onError(AjaxRequestTarget target) {
            target.add(feedback);
        }
    });
    // vote
    addButton(OmModalCloseButton.of());
    super.onInitialize();
}