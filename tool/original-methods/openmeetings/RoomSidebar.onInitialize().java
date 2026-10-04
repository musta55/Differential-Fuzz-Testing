@Override
protected void onInitialize() {
    super.onInitialize();
    final NameDialog addFolder = new NameDialog("addFolder", getString("712")) {

        private static final long serialVersionUID = 1L;

        @Override
        protected void onSubmit(AjaxRequestTarget target) {
            roomFiles.createFolder(target, getModelObject());
            super.onSubmit(target);
        }
    };
    roomFiles = new RoomFilePanel("tree", room, addFolder);
    add(fileTab.setVisible(!room.isInterview()), roomFiles.setVisible(!room.isInterview()));
    add(addFolder, settings);
    add(upload = new UploadDialog("upload", roomFiles));
    updateShowFiles(null);
    add(activities = new ActivitiesPanel("activities", room));
}