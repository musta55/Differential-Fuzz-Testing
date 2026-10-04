@Override
public Message getMessage(int index) throws MessagingException {
    File f;
    if (isFile) {
        f = folderPath;
    } else {
        f = new File(folderPath, String.format("%d.msg", index));
    }
    try (InputStream fis = new FileInputStream(f);
        InputStream bis = new BufferedInputStream(fis)) {
        return new MailFileMessage(this, bis, index);
    } catch (IOException e) {
        throw new MessagingException("Cannot open folder: " + e.getMessage(), e);
    }
}