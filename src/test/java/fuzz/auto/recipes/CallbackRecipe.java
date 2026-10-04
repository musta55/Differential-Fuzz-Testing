package fuzz.auto.recipes;

import javax.security.auth.callback.LanguageCallback;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.TextOutputCallback;
import javax.security.sasl.RealmCallback;

import com.code_intelligence.jazzer.api.FuzzedDataProvider;

/**
 * Builds a standard JAAS callback for the Callback interface, choosing the kind from the fuzz
 * bytes so every branch of a callback handler is reached (DefaultCallbackHandler.processCallback
 * handles Name, Password, Realm and TextOutput callbacks and rejects everything else). Always
 * building a NameCallback left four of its five branches untested.
 */
final class CallbackRecipe implements Recipe
{
  @Override
  public Object build(FuzzedDataProvider data, Class<?> type)
  {
    switch (data.consumeInt(0, 4)) {
      case 0:
        return new NameCallback(prompt(data));
      case 1:
        return new PasswordCallback(prompt(data), data.consumeBoolean());
      case 2:
        return new RealmCallback(prompt(data));
      case 3:
        // The constructor accepts only INFORMATION(0), WARNING(1) and ERROR(2).
        return new TextOutputCallback(data.consumeInt(0, 2), prompt(data));
      default:
        return new LanguageCallback(); // a kind handlers usually reject: the "unsupported" path
    }
  }

  /**
   * The prompt is an argument of the callback's constructor, not of the method under test, and
   * the JDK constructors reject a null or empty one with IllegalArgumentException. Passing it
   * through would only make the recipe fail and the input be wasted, never test the handler.
   */
  private static String prompt(FuzzedDataProvider data)
  {
    String s = data.consumeAsciiString(32);
    return s.isEmpty() ? "x" : s;
  }
}
