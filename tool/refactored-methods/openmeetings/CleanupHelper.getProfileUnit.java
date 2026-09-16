public static CleanupEntityUnit getProfileUnit(final UserDao udao) {
    File parent = OmFileHelper.getUploadProfilesDir();
    List<File> invalid = new ArrayList<>();
    List<File> deleted = new ArrayList<>();
    int missing = 0;
    for (File profile : listFiles(parent)) {
        long userId = extractUserIdFromProfileName(profile.getName());
        User user = udao.get(userId);
        if (isInvalidProfile(profile, userId, user)) {
            invalid.add(profile);
        } else if (user.isDeleted()) {
            deleted.add(profile);
        }
    }
    for (User user : udao.getAllBackupUsers()) {
        if (!user.isDeleted() && user.getPictureUri() != null && !profileFileExists(user)) {
            missing++;
        }
    }
    return new CleanupEntityUnit(parent, invalid, deleted, missing);
}
// ---- helper method(s) introduced by the refactoring ----
private static File[] listFiles(File directory) {
    return list(directory, null);
}

private static File[] listMp4Files(File directory) {
    return list(directory, (dir, name) -> name.endsWith(EXTENSION_MP4));
}

private static File[] listFilesByHash(String hash) {
    return list(hibernateDir, (dir, name) -> name.startsWith(hash));
}

private static boolean isInvalidProfile(File profile, long userId, User user) {
    return profile.isFile() || userId < 0 || user == null;
}

private static boolean profileFileExists(User user) {
    return new File(OmFileHelper.getUploadProfilesUserDir(user.getId()), user.getPictureUri()).exists();
}

private static long extractUserIdFromProfileName(String name) {
    long result = -1;
    if (name.startsWith(OmFileHelper.PROFILES_PREFIX)) {
        try {
            result = Long.parseLong(name.substring(OmFileHelper.PROFILES_PREFIX.length()));
        } catch (NumberFormatException e) {
            log.error("Error parsing user ID from profile name: {}", name, e);
        }
    }
    return result;
}

private static String extractHashFromFileName(String fileName) {
    return fileName.substring(0, fileName.length() - EXTENSION_MP4.length() - 1);
}

