package fuzz.auto.recipes;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;

import com.code_intelligence.jazzer.api.FuzzedDataProvider;

/**
 * skywalking configuration providers (etcd): their settings field is filled by the
 * module system through newConfigCreator().onInitialized(...). Created with new, it stayed null and
 * every initConfigReader() call threw NullPointerException on its first line ("body never
 * executed"). Here the settings object is real and each of the named String fields is null, empty
 * or a valid-looking value, so every validation branch is reachable. The valid value points at a
 * closed local port, so a client that does get created never reaches a real server.
 *
 * <p>Not used for zookeeper: its valid settings start a live client whose background state
 * differs from run to run, a false DIVERGENT. Reflection only: this folder is compiled for every
 * project.
 */
final class ConfigProviderRecipe implements Recipe
{
  /** Pairs of (settings field, valid value). */
  private final String[] fieldsAndValues;

  ConfigProviderRecipe(String... fieldsAndValues)
  {
    this.fieldsAndValues = fieldsAndValues;
  }

  @Override
  public Object build(FuzzedDataProvider data, Class<?> type) throws Exception
  {
    Object provider = type.getConstructor().newInstance();
    Field settingsField = type.getDeclaredField("settings");
    settingsField.setAccessible(true);
    Constructor<?> ctor = settingsField.getType().getDeclaredConstructor();
    ctor.setAccessible(true);
    Object settings = ctor.newInstance();

    for (int i = 0; i < fieldsAndValues.length; i += 2) {
      Field f = settings.getClass().getDeclaredField(fieldsAndValues[i]);
      f.setAccessible(true);
      String[] choices = {null, "", fieldsAndValues[i + 1]};
      f.set(settings, choices[data.consumeInt(0, 2)]);
    }
    settingsField.set(provider, settings);
    return provider;
  }
}
