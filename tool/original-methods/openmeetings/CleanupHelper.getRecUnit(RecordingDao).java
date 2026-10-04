public static CleanupEntityUnit getRecUnit(final RecordingDao recordDao) {
    File parent = OmFileHelper.getStreamsDir();
    List<File> invalid = new ArrayList<>();
    List<File> deleted = new ArrayList<>();
    int missing = 0;
    for (File f : list(hibernateDir, (dir, name) -> name.endsWith(EXTENSION_MP4))) {
        if (!f.isFile()) {
            log.warn("Recording found is not a file: {}", f);
            continue;
        }
        String hash = f.getName().substring(0, f.getName().length() - EXTENSION_MP4.length() - 1);
        Recording item = recordDao.getAny(hash, Recording.class);
        if (item == null) {
            addAll(invalid, list(hash));
        } else if (item.isDeleted()) {
            addAll(deleted, list(hash));
        }
    }
    for (Recording item : recordDao.get()) {
        if (!item.isDeleted() && item.getHash() != null && list(item.getHash()).length == 0) {
            missing++;
        }
    }
    return new CleanupEntityUnit(parent, invalid, deleted, missing) {

        private static final long serialVersionUID = 1L;

        @Override
        public void cleanup() throws IOException {
            String hiberPath = hibernateDir.getCanonicalPath();
            for (File f : list(getParent(), null)) {
                if (!f.getCanonicalPath().equals(hiberPath)) {
                    FileUtils.deleteQuietly(f);
                }
            }
            super.cleanup();
        }
    };
}