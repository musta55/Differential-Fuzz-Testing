public void init() {
    Injector.get().inject(this);
    extraMenu = createExtraMenu();
    List<Long> groups = retrieveGroups();
    addMenuItems(groups);
    extraMenu.setVisible(extraMenu.hasItems());
}
// ---- helper method(s) introduced by the refactoring ----
private OmMenuItem createExtraMenu() {
    return new OmMenuItem(room.getString("menu.extras"), null, false);
}

private List<Long> retrieveGroups() {
    return roomDao.get(room.getRoom().getId()).getGroups().stream().map(RoomGroup::getGroup).map(Group::getId).toList();
}

private void addMenuItems(List<Long> groups) {
    for (ExtraMenu em : menuDao.getByGroups(groups)) {
        extraMenu.add(createMenuItem(em));
    }
}

private OmMenuItem createMenuItem(ExtraMenu em) {
    return new OmMenuItem(em.getName(), em.getDescription()) {

        private static final long serialVersionUID = 1L;

        @Override
        public void onClick(AjaxRequestTarget target) {
            target.appendJavaScript(String.format("window.open('%s', '_blank');", em.getLink()));
        }
    };
}

