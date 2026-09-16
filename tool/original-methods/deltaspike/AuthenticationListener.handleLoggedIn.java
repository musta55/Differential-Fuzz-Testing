public void handleLoggedIn(@Observes UserEvent.LoggedIn event) {
    this.viewNavigationHandler.navigateTo(loggedInAccessDecisionVoter.getDeniedPage());
    System.err.println("handling loggedin");
}