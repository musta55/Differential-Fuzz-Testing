public List<LocatedFileStatus> listFilesInfo(String dir) throws IOException {
    List<LocatedFileStatus> files = new ArrayList<>();
    Path path = new Path(dir);
    FileStatus fileStatus = fileSystem.getFileStatus(path);
    if (!fileStatus.isDirectory()) {
        throw new FileNotFoundException("Cannot read directory " + dir);
    }
    RemoteIterator<LocatedFileStatus> it = fileSystem.listFiles(path, false);
    while (it.hasNext()) {
        LocatedFileStatus lfs = it.next();
        files.add(lfs);
    }
    return files;
}