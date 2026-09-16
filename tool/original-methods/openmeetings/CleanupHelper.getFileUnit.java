public static CleanupEntityUnit getFileUnit(final FileItemDao fileDao) {
    File parent = OmFileHelper.getUploadFilesDir();
    List<File> invalid = new ArrayList<>();
    List<File> deleted = new ArrayList<>();
    int missing = 0;
    for (File f : list(parent, null)) {
        FileItem item = fileDao.get(f.getName(), FileItem.class);
        if (item == null) {
            invalid.add(f);
        } else if (item.isDeleted()) {
            deleted.add(f);
        }
    }
    for (FileItem item : fileDao.get()) {
        if (!item.isDeleted() && !item.exists()) {
            missing++;
        }
    }
    return new CleanupEntityUnit(parent, invalid, deleted, missing);
}