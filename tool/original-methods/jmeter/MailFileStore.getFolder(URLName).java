@Override
public Folder getFolder(URLName path) throws MessagingException {
    return new MailFileFolder(this, path);
}