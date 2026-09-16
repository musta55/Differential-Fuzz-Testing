public static CleanupEntityUnit getRecUnit(final RecordingDao recordDao) {
    File parent = OmFileHelper.getStreamsDir();
    List<File> invalid = new ArrayList<>();
    List<File> deleted = new ArrayList<>();
    int missing = 0;
    for (File file : listMp4Files(hibernateDir)) {
        if (!file.isFile()) {
            log.warn("Recording found is not a file: {}", file);
            continue;
        }
        String hash = extractHashFromFileName(file.getName());
        Recording item = recordDao.getAny(hash, Recording.class);
        if (item == null) {
            addAll(invalid, listFilesByHash(hash));
        } else if (item.isDeleted()) {
            addAll(deleted, listFilesByHash(hash));
        }
    }
    for (Recording item : recordDao.get()) {
        if (!item.isDeleted() && item.getHash() != null && listFilesByHash(item.getHash()).length == 0) {
            missing++;
        }
    }
    return new CleanupEntityUnit(parent, invalid, deleted, missing) {

        private static final long serialVersionUID = 1L;

        @Override
        public void cleanup() throws IOException {
            String hiberPath = hibernateDir.getCanonicalPath();
            for (File file : listFiles(getParent())) {
                if (!file.getCanonicalPath().equals(hiberPath)) {
                    FileUtils.deleteQuietly(file);
                }
            }
            super.cleanup();
        }
    };
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

