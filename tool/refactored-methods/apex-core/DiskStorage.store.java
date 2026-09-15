@Override
public int store(String identifier, byte[] bytes, int startingOffset, int endingOffset) {
    int lUniqueIdentifier;
    String normalizedFileName = normalizeFileName(identifier);
    File directory = new File(basePath, normalizedFileName);
    if (directory.exists()) {
        File identityFile = new File(directory, "identity");
        if (identityFile.isFile()) {
            try {
                byte[] stored = java.nio.file.Files.readAllBytes(identityFile.toPath());
                if (Arrays.equals(stored, identifier.getBytes())) {
                    synchronized (this) {
                        lUniqueIdentifier = ++this.uniqueIdentifier;
                    }
                } else {
                    throw new IllegalStateException("Collision in identifier name, please ensure that the slug for " + "the identifiers is different");
                }
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        } else {
            throw new IllegalStateException("Identity file is hijacked!");
        }
    } else {
        if (directory.mkdirs()) {
            File identity = new File(directory, "identity");
            try {
                java.nio.file.Files.write(identity.toPath(), identifier.getBytes());
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        } else {
            throw new RuntimeException("directory " + directory.getAbsolutePath() + " could not be created!");
        }
        lUniqueIdentifier = ++this.uniqueIdentifier;
    }
    try {
        return writeFile(bytes, startingOffset, endingOffset, directory, lUniqueIdentifier);
    } catch (IOException ex) {
        throw new RuntimeException(ex);
    }
}