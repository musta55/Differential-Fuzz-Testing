private static File[] list(File f, FilenameFilter ff) {
    File[] l = ff == null ? f.listFiles() : f.listFiles(ff);
    return l == null ? new File[0] : l;
}