@Override
protected void checkPermission(AccessDecisionVoterContext context, Set<SecurityViolation> violations) {
    if (loginController.isLoggedIn()) {
        // no violations, pass
    } else {
        violations.add(new SecurityViolation() {

            @Override
            public String getReason() {
                return "User must be logged in to access this resource";
            }
        });
        // remember the requested page
        deniedPage = viewConfigResolver.getViewConfigDescriptor(FacesContext.getCurrentInstance().getViewRoot().getViewId()).getConfigClass();
    }
}