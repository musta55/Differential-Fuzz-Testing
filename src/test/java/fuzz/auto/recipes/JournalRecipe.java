package fuzz.auto.recipes;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;

import com.code_intelligence.jazzer.api.FuzzedDataProvider;

/**
 * Journal(StreamingContainerManager scm) needs the whole application master, which the engine
 * cannot build. Journal only stores scm and write() never uses it (only replay() does), so null
 * is enough. A new Journal has no output stream and write() then skips writing, so most journals
 * get an in-memory stream; the rest keep the "output stream is null" path in the input space.
 */
final class JournalRecipe implements Recipe
{
  @Override
  public Object build(FuzzedDataProvider data, Class<?> type) throws Exception
  {
    Class<?> scmType = Class.forName("com.datatorrent.stram.StreamingContainerManager", false,
        type.getClassLoader());
    Object journal = type.getConstructor(scmType).newInstance((Object) null);
    if (data.consumeInt(0, 7) != 0) {
      type.getMethod("setOutputStream", OutputStream.class)
          .invoke(journal, new ByteArrayOutputStream());
    }
    return journal;
  }
}
