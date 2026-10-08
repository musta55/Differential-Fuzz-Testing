package fuzz.auto.recipes;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import com.code_intelligence.jazzer.api.FuzzedDataProvider;

/**
 * java.lang.annotation.Annotation: annotation instances only come from reading an annotated
 * element, so the engine could not build one (AnnotationUtils.getQualifierHashCode). These are read
 * from the fields below: no members, every kind of member at its default, and every kind filled.
 */
final class AnnotationRecipe implements Recipe
{
  @Retention(RetentionPolicy.RUNTIME)
  @interface Marker
  {
  }

  @Retention(RetentionPolicy.RUNTIME)
  @interface Sample
  {
    int number() default 0;
    String text() default "";
    int[] ints() default {};
    long[] longs() default {};
    short[] shorts() default {};
    double[] doubles() default {};
    float[] floats() default {};
    boolean[] booleans() default {};
    byte[] bytes() default {};
    char[] chars() default {};
    String[] texts() default {};
  }

  @Marker
  private static final Object MARKER = null;

  @Sample
  private static final Object DEFAULTS = null;

  @Sample(number = 7, text = "a", ints = {1, 2}, longs = {3L}, shorts = {4}, doubles = {0.5},
      floats = {1.5f}, booleans = {true}, bytes = {5}, chars = {'x'}, texts = {"b", "c"})
  private static final Object FILLED = null;

  /** FILLED first: a used-up input reads as 0 and gets the annotation that reaches every branch. */
  private static final String[] FIELDS = {"FILLED", "DEFAULTS", "MARKER"};

  @Override
  public Object build(FuzzedDataProvider data, Class<?> type) throws Exception
  {
    String field = FIELDS[data.consumeInt(0, FIELDS.length - 1)];
    return AnnotationRecipe.class.getDeclaredField(field).getAnnotations()[0];
  }
}
