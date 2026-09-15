package org.apache.deltaspike.jsf.api.literal;

import org.apache.deltaspike.core.util.ClassUtils;
import org.apache.deltaspike.jsf.api.config.base.JsfBaseConfig;
import org.apache.deltaspike.jsf.api.config.view.Folder;

import javax.enterprise.util.AnnotationLiteral;
import java.util.Objects;

/**
 * Literal for {@link Folder}
 */
//TODO remove null trick once we can merge with default values and the tests pass
public class FolderLiteral extends AnnotationLiteral<Folder> implements Folder
{
    private static final long serialVersionUID = 2582580975876369665L;

    private final String name;
    private final Class<? extends NameBuilder> folderNameBuilder;

    public FolderLiteral(boolean virtual)
    {
        this.name = virtual ? null : "";
        final String customDefaultFolderNameBuilderClassName =
            JsfBaseConfig.ViewConfigCustomization.CUSTOM_DEFAULT_FOLDER_NAME_BUILDER;
        this.folderNameBuilder = ClassUtils.tryToLoadClassForName(customDefaultFolderNameBuilderClassName) != null ?
            ClassUtils.tryToLoadClassForName(customDefaultFolderNameBuilderClassName) :
            DefaultFolderNameBuilder.class;
    }

    public FolderLiteral(String name, Class<? extends NameBuilder> folderNameBuilder)
    {
        this.name = name;
        this.folderNameBuilder = folderNameBuilder;
    }

    @Override
    public String name()
    {
        return this.name;
    }

    @Override
    public Class<? extends NameBuilder> folderNameBuilder()
    {
        return this.folderNameBuilder;
    }

    /*
    * generated
    */

    @Override
    public boolean equals(Object o)
    {
        if (this == o)
        {
            return true;
        }
        if (!(o instanceof FolderLiteral))
        {
            return false;
        }
        FolderLiteral that = (FolderLiteral) o;
        return super.equals(o) &&
               Objects.equals(folderNameBuilder, that.folderNameBuilder) &&
               Objects.equals(name, that.name);
    }

    @Override
    public int hashCode()
    {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + folderNameBuilder.hashCode();
        return result;
    }
}