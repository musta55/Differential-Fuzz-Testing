public void init() {
    Injector.get().inject(this);
    extraMenu = new OmMenuItem(room.getString("menu.extras"), null, false);
    List<Long> groups = roomDao.get(room.getRoom().getId()).getGroups().stream().map(RoomGroup::getGroup).map(Group::getId).toList();
    for (ExtraMenu em : menuDao.getByGroups(groups)) {
        extraMenu.add(new OmMenuItem(em.getName(), em.getDescription()) {

            private static final long serialVersionUID = 1L;

            @Override
            public void onClick(AjaxRequestTarget target) {
                target.appendJavaScript(String.format("window.open('%s', '_blank');", em.getLink()));
            }
        });
    }
    extraMenu.setVisible(extraMenu.hasItems());
}