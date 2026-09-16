public void handleFailed(@Observes UserEvent.LoginFailed event) {
    this.viewNavigationHandler.navigateTo(Pages.Login.class);
}