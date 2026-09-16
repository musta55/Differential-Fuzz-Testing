@Override
public void open(int mode) throws MessagingException {
    if (mode != READ_ONLY) {
        throw new MessagingException("Implementation only supports read-only access");
    }
    this.mode = mode;
    isOpen = true;
}