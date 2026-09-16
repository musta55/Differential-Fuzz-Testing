@Override
protected void checkPermission(AccessDecisionVoterContext context, Set<SecurityViolation> violations) {
    if (!loginController.isLoggedIn()) {
        violations.add(createSecurityViolation());
        // remember the requested page
        deniedPage = viewConfigResolver.getViewConfigDescriptor(FacesContext.getCurrentInstance().getViewRoot().getViewId()).getConfigClass();
    }
}
// ---- helper method(s) introduced by the refactoring ----
private SecurityViolation createSecurityViolation() {
    return new SecurityViolation() {

        @Override
        public String getReason() {
            return "User must be logged in to access this resource";
        }
    };
}

