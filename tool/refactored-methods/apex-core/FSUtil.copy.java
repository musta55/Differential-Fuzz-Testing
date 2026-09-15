/**
 * Copied from FileUtil to transfer ownership
 *
 * @param srcFS
 * @param srcStatus
 * @param dstFS
 * @param dst
 * @param deleteSource
 * @param overwrite
 * @param conf
 * @return
 * @throws IOException
 */
public static boolean copy(FileSystem srcFS, FileStatus srcStatus, FileSystem dstFS, Path dst, boolean deleteSource, boolean overwrite, Configuration conf) throws IOException {
    Path src = srcStatus.getPath();
    if (srcStatus.isDirectory()) {
        if (!createAndSetPermissionsForDirectory(dstFS, dst)) {
            return false;
        }
        copyDirectoryContents(srcFS, srcStatus, dstFS, dst, deleteSource, overwrite, conf);
    } else {
        copyFile(srcFS, src, dstFS, dst, overwrite, conf);
        setFilePermissions(dstFS, dst, new FsPermission((short) 0777));
    }
    return deleteSource ? srcFS.delete(src, true) : true;
}
// ---- helper method(s) introduced by the refactoring ----
private static boolean createAndSetPermissionsForDirectory(FileSystem fs, Path path) throws IOException {
    if (!mkdirs(fs, path)) {
        return false;
    }
    setFilePermissions(fs, path, new FsPermission((short) 0777));
    return true;
}

private static void copyDirectoryContents(FileSystem srcFS, FileStatus srcStatus, FileSystem dstFS, Path dst, boolean deleteSource, boolean overwrite, Configuration conf) throws IOException {
    FileStatus[] contents = srcFS.listStatus(srcStatus.getPath());
    for (FileStatus content : contents) {
        copy(srcFS, content, dstFS, new Path(dst, content.getPath().getName()), deleteSource, overwrite, conf);
    }
}

private static void copyFile(FileSystem srcFS, Path src, FileSystem dstFS, Path dst, boolean overwrite, Configuration conf) throws IOException {
    try (InputStream in = srcFS.open(src);
        OutputStream out = dstFS.create(dst, overwrite)) {
        org.apache.hadoop.io.IOUtils.copyBytes(in, out, conf, true);
    }
}

private static void setFilePermissions(FileSystem fs, Path path, FsPermission permission) throws IOException {
    fs.setPermission(path, permission);
}

private static boolean createLocalDirectory(File dir) {
    return dir.exists() || dir.mkdirs();
}

private static void setLocalDirectoryPermissions(RawLocalFileSystem localFileSystem, File dir) throws IOException {
    FsPermission permissions = new FsPermission(FsAction.ALL, FsAction.NONE, FsAction.NONE);
    localFileSystem.setPermission(new Path(dir.getAbsolutePath()), permissions);
}

private static void setLocalFilePermissions(RawLocalFileSystem localFileSystem, File file) throws IOException {
    FsPermission permissions = new FsPermission(FsAction.READ, FsAction.NONE, FsAction.NONE);
    localFileSystem.setPermission(new Path(file.getAbsolutePath()), permissions);
}

