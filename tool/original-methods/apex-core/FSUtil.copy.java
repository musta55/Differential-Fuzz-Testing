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
    //dst = checkDest(src.getName(), dstFS, dst, overwrite);
    if (srcStatus.isDirectory()) {
        //checkDependencies(srcFS, src, dstFS, dst);
        if (!mkdirs(dstFS, dst)) {
            return false;
        }
        FileStatus[] contents = srcFS.listStatus(src);
        for (int i = 0; i < contents.length; i++) {
            copy(srcFS, contents[i], dstFS, new Path(dst, contents[i].getPath().getName()), deleteSource, overwrite, conf);
        }
    } else {
        try (InputStream in = srcFS.open(src);
            OutputStream out = dstFS.create(dst, overwrite)) {
            org.apache.hadoop.io.IOUtils.copyBytes(in, out, conf, true);
        }
    }
    // TODO: change group and limit write to group
    if (srcStatus.isDirectory()) {
        dstFS.setPermission(dst, new FsPermission((short) 0777));
    } else {
        dstFS.setPermission(dst, new FsPermission((short) 0777));
    }
    //dstFS.setOwner(dst, null, srcStatus.getGroup());
    /*
    try {
      // transfer owner
      // DOES NOT WORK only super user can change file owner
      dstFS.setOwner(dst, srcStatus.getOwner(), srcStatus.getGroup());
    } catch (IOException e) {
      LOG.warn("Failed to change owner on {} to {}", dst, srcStatus.getOwner(), e);
      throw e;
    }
*/
    if (deleteSource) {
        return srcFS.delete(src, true);
    } else {
        return true;
    }
}