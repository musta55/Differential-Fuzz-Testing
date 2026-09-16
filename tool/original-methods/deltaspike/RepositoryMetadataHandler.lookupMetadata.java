/**
 * Lookup the Repository component meta data from a list of candidate classes.
 * Depending on the implementation, proxy objects might have been modified so the actual class
 * does not match the original Repository class.
 *
 * @param candidateClasses List of candidates to check.
 * @return A {@link RepositoryMetadataInitializer}.
 */
public RepositoryMetadata lookupMetadata(List<Class<?>> candidateClasses) {
    for (Class<?> repoClass : candidateClasses) {
        if (repositoriesMetadata.containsKey(repoClass)) {
            return repositoriesMetadata.get(repoClass);
        }
    }
    throw new RuntimeException("Unknown Repository classes " + candidateClasses);
}