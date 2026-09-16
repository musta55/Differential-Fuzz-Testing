package org.apache.deltaspike.example.metadata;

import org.apache.deltaspike.core.api.literal.NamedLiteral;
import org.apache.deltaspike.core.util.metadata.builder.AnnotatedTypeBuilder;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.Extension;
import javax.enterprise.inject.spi.ProcessAnnotatedType;
import javax.inject.Named;

/**
 * Just a test filter to show the basic functionality provided by {@link AnnotatedTypeBuilder}
 */
public class NamingConventionAwareMetadataFilter implements Extension
{
    public void ensureNamingConvention(@Observes ProcessAnnotatedType processAnnotatedType)
    {
        Class<?> beanClass = processAnnotatedType.getAnnotatedType().getJavaClass();

        Named namedAnnotation = beanClass.getAnnotation(Named.class);
        if (namedAnnotation != null && shouldModifyBeanName(namedAnnotation.value()))
        {
            AnnotatedTypeBuilder builder = new AnnotatedTypeBuilder();
            builder.readFromType(beanClass);

            String newBeanName = modifyBeanName(namedAnnotation.value());

            builder.removeFromClass(Named.class)
                    .addToClass(new NamedLiteral(newBeanName));

            processAnnotatedType.setAnnotatedType(builder.create());
        }
    }

    private boolean shouldModifyBeanName(String beanName)
    {
        return beanName.length() > 0 && Character.isUpperCase(beanName.charAt(0));
    }

    private String modifyBeanName(String beanName)
    {
        return beanName.substring(0, 1).toLowerCase() + beanName.substring(1);
    }
}