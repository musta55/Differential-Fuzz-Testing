@Override
public NavigationCase getNavigationCase(FacesContext context, String fromAction, String outcome) {
    return this.deltaSpikeNavigationHandler.getNavigationCase(context, fromAction, outcome);
}