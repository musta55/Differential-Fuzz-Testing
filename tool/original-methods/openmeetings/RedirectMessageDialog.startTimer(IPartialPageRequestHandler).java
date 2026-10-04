private void startTimer(IPartialPageRequestHandler handler) {
    getLabel().add(new OmTimerBehavior(DELAY, labelId) {

        private static final long serialVersionUID = 1L;

        @Override
        protected void onFinish(AjaxRequestTarget target) {
            if (Strings.isEmpty(url)) {
                throw new RestartResponseException(Application.get().getHomePage());
            } else {
                throw new RedirectToUrlException(url);
            }
        }
    });
    if (handler != null) {
        handler.add(getLabel());
    }
}