public List<String> listFiles(String dir) throws IOException {
    List<String> files = new ArrayList<>();
    Path path = new Path(dir);
    if (!fileSystem.isDirectory(path)) {
        throw new FileNotFoundException("Cannot read directory " + dir);
    }
    RemoteIterator<LocatedFileStatus> it = fileSystem.listFiles(path, false);
    while (it.hasNext()) {
        LocatedFileStatus lfs = it.next();
        files.add(lfs.getPath().getName());
    }
    return files;
}