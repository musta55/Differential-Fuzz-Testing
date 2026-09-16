public MailFileFolder(Store store, String path) {
    super(store);
    // == ServerName from mail sampler
    String base = store.getURLName().getHost();
    File parentFolder = new File(base);
    isFile = parentFolder.isFile();
    if (isFile) {
        folderPath = new File(base);
    } else {
        folderPath = new File(base, path);
    }
}