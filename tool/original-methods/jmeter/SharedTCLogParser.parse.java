/**
 * {@inheritDoc}
 */
@Override
public int parse(TestElement el, int parseCount) {
    FileServer fileServer = FileServer.getFileServer();
    fileServer.reserveFile(FILENAME);
    try {
        return parse(fileServer, el, parseCount);
    } catch (Exception exception) {
        log.error("Problem creating samples", exception);
    }
    // indicate that an error occurred
    return -1;
}