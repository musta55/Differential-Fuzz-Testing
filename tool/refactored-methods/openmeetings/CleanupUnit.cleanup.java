/**
 * @throws IOException some of the subclasses can throw
 */
public void cleanup() throws IOException {
    if (parent.listFiles() != null) {
        for (File f : parent.listFiles()) {
            try {
                FileUtils.forceDelete(f);
            } catch (IOException e) {
                LOG.log(Level.SEVERE, "Failed to delete file: " + f.getAbsolutePath(), e);
                throw e;
            }
        }
    }
}