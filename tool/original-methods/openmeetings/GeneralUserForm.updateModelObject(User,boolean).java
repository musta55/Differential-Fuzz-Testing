public void updateModelObject(User u, boolean isAdminForm) {
    grpUsers.clear();
    grpUsers.addAll(u.getGroupUsers());
    if (isAdminForm) {
        List<Group> grpList = hasGroupAdminLevel(getRights()) ? groupDao.adminGet(null, getUserId(), 0, Integer.MAX_VALUE, null) : groupDao.get(0, Integer.MAX_VALUE);
        for (Group g : grpList) {
            GroupUser gu = new GroupUser(g, u);
            int idx = grpUsers.indexOf(gu);
            if (idx < 0) {
                grpUsers.add(gu);
            }
        }
    }
}