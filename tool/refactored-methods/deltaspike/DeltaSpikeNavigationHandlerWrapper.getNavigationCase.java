@Override
public NavigationCase getNavigationCase(FacesContext context, String fromAction, String outcome) {
    return deltaSpikeNavigationHandler.getNavigationCase(context, fromAction, outcome);
}