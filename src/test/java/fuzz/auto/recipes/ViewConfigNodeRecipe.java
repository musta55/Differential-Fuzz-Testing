package fuzz.auto.recipes;

import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Set;

import com.code_intelligence.jazzer.api.FuzzedDataProvider;

/**
 * deltaspike ViewConfigNode: an interface the engine found no implementation of
 * (FolderConfigNode(ViewConfigNode, Class)). A FolderConfigNode at the root, with no parent and no
 * metadata, is a real node of its own.
 */
final class ViewConfigNodeRecipe implements Recipe
{
  @Override
  public Object build(FuzzedDataProvider data, Class<?> type) throws Exception
  {
    Class<?> folderType = Class.forName("org.apache.deltaspike.jsf.impl.config.view.FolderConfigNode",
        true, type.getClassLoader());
    Set<Annotation> metaData = new HashSet<Annotation>();
    return folderType.getConstructor(Class.class, type, Set.class).newInstance(Object.class, null, metaData);
  }
}
