package fuzz.auto.recipes;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Files;

import com.code_intelligence.jazzer.api.FuzzedDataProvider;

/**
 * Hadoop Path: a random path string points to no file, so file-reading methods only ever hit
 * "file not found". This writes the fuzzed bytes to a temp file and returns its path; now and then
 * it returns a path that does not exist, to keep that case too.
 *
 * <p>Both sides get the same path (one shared folder, file name from the fuzz bytes), so paths in
 * results compare equal. A method that deletes or changes the file runs on the original side first
 * and the refactored side then sees the changed file.
 */
final class PathRecipe implements Recipe
{
  private static File dir;

  @Override
  public Object build(FuzzedDataProvider data, Class<?> type) throws Exception
  {
    File file = new File(dir(), "f" + data.consumeInt(0, 9));
    if (data.consumeInt(0, 7) == 0) {
      file.delete(); // a missing file
    } else {
      try (FileOutputStream out = new FileOutputStream(file)) {
        out.write(data.consumeBytes(64));
      }
    }
    return type.getConstructor(String.class).newInstance(file.getAbsolutePath());
  }

  private static synchronized File dir() throws Exception
  {
    if (dir == null) {
      dir = Files.createTempDirectory("fuzzpath").toFile();
    }
    return dir;
  }
}
