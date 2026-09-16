@Override
protected void onInitialize() {
    super.onInitialize();
    User u = userDao.get(getUserId());
    List<AbstractTab> tabs = createTabs(u.getGroupUsers().stream());
    add(new AjaxBootstrapTabbedPanel<>("orgTabs", tabs));
}
// ---- helper method(s) introduced by the refactoring ----
private List<AbstractTab> createTabs(Stream<GroupUser> groupUsers) {
    return groupUsers.map(GroupUser::getGroup).filter(g -> !roomDao.getGroupRooms(g.getId()).isEmpty()).map(g -> new AbstractTab(Model.of(g.getName())) {

        private static final long serialVersionUID = 1L;

        @Override
        public WebMarkupContainer getPanel(String panelId) {
            return new RoomsPanel(panelId, roomDao.getGroupRooms(g.getId()));
        }
    }).collect(Collectors.toList());
}

