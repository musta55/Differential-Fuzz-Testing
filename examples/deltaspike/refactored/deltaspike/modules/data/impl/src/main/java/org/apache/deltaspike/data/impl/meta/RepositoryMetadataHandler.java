package org.apache.deltaspike.data.impl.meta;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;
import org.apache.deltaspike.data.impl.RepositoryExtension;

@ApplicationScoped
public class RepositoryMetadataHandler
{
    private final Map<Class<?>, RepositoryMetadata> repositoriesMetadata =
            new ConcurrentHashMap<>();

    @Inject
    private BeanManager beanManager;
    @Inject
    private RepositoryExtension extension;
    
    @Inject
    private RepositoryMetadataInitializer metadataInitializer;

    @PostConstruct
    public void init()
    {
        for (Class<?> repositoryClass : extension.getRepositoryClasses())
        {
            RepositoryMetadata metadata = metadataInitializer.init(repositoryClass, beanManager);
            repositoriesMetadata.put(repositoryClass, metadata);
        }
    }

    /**
     * Lookup the Repository component meta data from a list of candidate classes.
     * Depending on the implementation, proxy objects might have been modified so the actual class
     * does not match the original Repository class.
     *
     * @param candidateClasses List of candidates to check.
     * @return A {@link RepositoryMetadataInitializer}.
     */
    public RepositoryMetadata lookupMetadata(List<Class<?>> candidateClasses)
    {
        for (Class<?> repoClass : candidateClasses)
        {
            if (repositoriesMetadata.containsKey(repoClass))
            {
                return repositoriesMetadata.get(repoClass);
            }
        }
        throw new IllegalArgumentException("Unknown Repository classes " + candidateClasses);
    }
    
    /**
     * lookup the {@link RepositoryMethodMetadata} for a specific repository and method.
     *
     * @param repositoryMetadata The Repository metadata to lookup the method for.
     * @param method The method object to get Repository meta data for.
     * @return A {@link RepositoryMethodMetadata}.
     */
    public RepositoryMethodMetadata lookupMethodMetadata(RepositoryMetadata repositoryMetadata, Method method)
    {
        return repositoryMetadata.getMethodsMetadata().get(method);
    }

}