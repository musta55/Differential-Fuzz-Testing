public static void setPermission(FileSystem fs, Path dst, FsPermission permission) throws IOException {
    FileStatus[] contents = fs.listStatus(dst);
    for (int i = 0; i < contents.length; i++) {
        fs.setPermission(contents[i].getPath(), permission);
    }
    fs.setPermission(dst, permission);
}