@Override
protected void onInitialize() {
    super.onInitialize();
    User u = userDao.get(getUserId());
    Stream<Group> groups = u.getGroupUsers().stream().map(GroupUser::getGroup).filter(g -> !roomDao.getGroupRooms(g.getId()).isEmpty());
    add(new AjaxBootstrapTabbedPanel<>("orgTabs", groups.map(g -> new AbstractTab(Model.of(g.getName())) {

        private static final long serialVersionUID = 1L;

        @Override
        public WebMarkupContainer getPanel(String panelId) {
            return new RoomsPanel(panelId, roomDao.getGroupRooms(g.getId()));
        }
    }).toList()));
}