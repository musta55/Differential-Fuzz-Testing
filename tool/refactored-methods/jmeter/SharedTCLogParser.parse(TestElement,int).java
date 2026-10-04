/**
 * {@inheritDoc}
 */
@Override
public int parse(TestElement el, int parseCount) {
    FileServer fileServer = FileServer.getFileServer();
    fileServer.reserveFile(FILENAME);
    try {
        return parseFile(fileServer, el, parseCount);
    } catch (Exception exception) {
        log.error("Problem creating samples", exception);
    }
    // indicate that an error occurred
    return -1;
}
// ---- helper method(s) introduced by the refactoring ----
private int parseFile(FileServer fileServer, TestElement el, int parseCount) throws IOException {
    int actualCount = 0;
    String line;
    while ((line = readLine(fileServer)) != null && (parseCount == -1 || actualCount < parseCount)) {
        if (line.length() > 0) {
            actualCount += parseLine(line, el);
        }
    }
    if (line == null) {
        fileServer.closeFile(FILENAME);
    }
    return actualCount;
}

private String readLine(FileServer fileServer) throws IOException {
    return fileServer.readLine(FILENAME);
}

