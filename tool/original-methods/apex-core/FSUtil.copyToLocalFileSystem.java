/**
 * Download the file from dfs to local file.
 *
 * @param fs
 * @param destinationFile
 * @param dfsFile
 * @param conf
 * @return
 * @throws IOException
 */
public static File copyToLocalFileSystem(FileSystem fs, String destinationPath, String destinationFile, String dfsFile, Configuration conf) throws IOException {
    File destinationDir = new File(destinationPath);
    if (!destinationDir.exists() && !destinationDir.mkdirs()) {
        throw new RuntimeException("Unable to create local directory");
    }
    try (RawLocalFileSystem localFileSystem = new RawLocalFileSystem()) {
        // allow app user to access local dir
        FsPermission permissions = new FsPermission(FsAction.ALL, FsAction.NONE, FsAction.NONE);
        localFileSystem.setPermission(new Path(destinationDir.getAbsolutePath()), permissions);
        Path dfsFilePath = new Path(dfsFile);
        File localFile = new File(destinationDir, destinationFile);
        FileUtil.copy(fs, dfsFilePath, localFile, false, conf);
        // set permissions on actual file to be read-only for user
        permissions = new FsPermission(FsAction.READ, FsAction.NONE, FsAction.NONE);
        localFileSystem.setPermission(new Path(localFile.getAbsolutePath()), permissions);
        return localFile;
    }
}