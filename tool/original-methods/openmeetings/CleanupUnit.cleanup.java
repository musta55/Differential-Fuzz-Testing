/**
 * @throws IOException some of the subclussed can throw
 */
public void cleanup() throws IOException {
    for (File f : getParent().listFiles()) {
        FileUtils.deleteQuietly(f);
    }
}