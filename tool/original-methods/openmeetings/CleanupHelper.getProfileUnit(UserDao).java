public static CleanupEntityUnit getProfileUnit(final UserDao udao) {
    File parent = OmFileHelper.getUploadProfilesDir();
    List<File> invalid = new ArrayList<>();
    List<File> deleted = new ArrayList<>();
    int missing = 0;
    for (File profile : list(parent, null)) {
        long userId = getUserIdByProfile(profile.getName());
        User u = udao.get(userId);
        if (profile.isFile() || userId < 0 || u == null) {
            invalid.add(profile);
        } else if (u.isDeleted()) {
            deleted.add(profile);
        }
    }
    for (User u : udao.getAllBackupUsers()) {
        if (!u.isDeleted() && u.getPictureUri() != null && !new File(OmFileHelper.getUploadProfilesUserDir(u.getId()), u.getPictureUri()).exists()) {
            missing++;
        }
    }
    return new CleanupEntityUnit(parent, invalid, deleted, missing);
}