@Override
public void discard(String identifier, int uniqueIdentifier) {
    String normalizedFilename = normalizeFileName(identifier);
    File directory = new File(basePath, normalizedFilename);
    if (directory.exists()) {
        File identityFile = new File(directory, "identity");
        if (identityFile.isFile()) {
            try {
                byte[] stored = Files.toByteArray(identityFile);
                if (Arrays.equals(stored, identifier.getBytes())) {
                    File deletionFile = new File(directory, String.valueOf(uniqueIdentifier));
                    if (deletionFile.exists() && deletionFile.isFile()) {
                        if (!deletionFile.delete()) {
                            throw new RuntimeException("File " + deletionFile.getPath() + " could not be deleted!");
                        }
                    } else {
                        throw new RuntimeException("File " + deletionFile.getPath() + " either is non existent or not a file!");
                    }
                } else {
                    throw new RuntimeException("Collision in the identifier name, please ensure that the slugs for " + "the identifiers are different");
                }
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        } else {
            throw new RuntimeException(identityFile + " is not a file!");
        }
    } else {
        throw new RuntimeException("directory " + directory.getPath() + " does not exist!");
    }
}