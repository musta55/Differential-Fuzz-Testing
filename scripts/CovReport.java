import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.jacoco.core.analysis.Analyzer;
import org.jacoco.core.analysis.CoverageBuilder;
import org.jacoco.core.analysis.IClassCoverage;
import org.jacoco.core.analysis.ICounter;
import org.jacoco.core.analysis.IMethodCoverage;
import org.jacoco.core.tools.ExecFileLoader;

/**
 * Per-method branch/line coverage from a jacoco.exec.
 * Usage: CovReport <jacoco.exec> <classesDir> [classSimpleName] [methodName] [paramTypes]
 * Prints e.g.  DiskStorageRefactored.normalizeFileName branch=4/4 line=5/5
 *
 * <p>The class is matched EXACTLY. A substring match let "RequestTupleOriginal" also select
 * "SubscribeRequestTupleOriginal", and callers read only the first line printed, so a method was
 * reported with another class's numbers depending on iteration order.
 *
 * <p>paramTypes is the manifest's parameter list joined with ';' (source spellings, e.g.
 * "byte[];int;java.lang.String"). It picks one overload out of several same-name methods — every
 * constructor is named "&lt;init&gt;" — by comparing erased simple type names against the
 * bytecode descriptor. Omit it to print every method of that name.
 */
public final class CovReport
{
  public static void main(String[] a) throws Exception
  {
    ExecFileLoader loader = new ExecFileLoader();
    loader.load(new File(a[0]));
    CoverageBuilder cb = new CoverageBuilder();
    new Analyzer(loader.getExecutionDataStore(), cb).analyzeAll(new File(a[1]));
    String classFilter = a.length > 2 ? a[2] : "";
    String methodFilter = a.length > 3 ? a[3] : "";
    List<String> wantParams = a.length > 4 ? sourceParams(a[4]) : null;
    for (IClassCoverage c : cb.getClasses()) {
      String simple = c.getName().substring(c.getName().lastIndexOf('/') + 1);
      if (!classFilter.isEmpty() && !simple.equals(classFilter)) {
        continue;
      }
      for (IMethodCoverage m : c.getMethods()) {
        if (!methodFilter.isEmpty() && !m.getName().equals(methodFilter)) {
          continue;
        }
        if (wantParams != null && !wantParams.equals(descriptorParams(m.getDesc()))) {
          continue;
        }
        ICounter br = m.getBranchCounter();
        ICounter ln = m.getLineCounter();
        System.out.println(simple + "." + m.getName()
            + " branch=" + br.getCoveredCount() + "/" + br.getTotalCount()
            + " line=" + ln.getCoveredCount() + "/" + ln.getTotalCount());
      }
    }
  }

  /** "java.util.List<String>;int..." -> [List, int[]]: generics erased, package dropped. */
  private static List<String> sourceParams(String joined)
  {
    List<String> out = new ArrayList<>();
    if (joined.isEmpty()) {
      return out;
    }
    for (String t : joined.split(";")) {
      String erased = t.replaceAll("<.*>", "").replace("...", "[]").replace(" ", "");
      out.add(simpleName(erased));
    }
    return out;
  }

  /**
   * "([BILjava/util/List;)V" -> [byte[], int, List]. Parsed by hand: callers compile this against
   * jacoco-core alone, which does not bundle ASM's Type.
   */
  private static List<String> descriptorParams(String desc)
  {
    List<String> out = new ArrayList<>();
    int i = desc.indexOf('(') + 1;
    while (desc.charAt(i) != ')') {
      int dims = 0;
      while (desc.charAt(i) == '[') {
        dims++;
        i++;
      }
      String name;
      char k = desc.charAt(i);
      if (k == 'L') {
        int end = desc.indexOf(';', i);
        name = simpleName(desc.substring(i + 1, end).replace('/', '.'));
        i = end + 1;
      } else {
        name = primitive(k);
        i++;
      }
      StringBuilder sb = new StringBuilder(name);
      for (int d = 0; d < dims; d++) {
        sb.append("[]");
      }
      out.add(sb.toString());
    }
    return out;
  }

  private static String primitive(char k)
  {
    switch (k) {
      case 'Z': return "boolean";
      case 'B': return "byte";
      case 'C': return "char";
      case 'S': return "short";
      case 'I': return "int";
      case 'J': return "long";
      case 'F': return "float";
      case 'D': return "double";
      default: throw new IllegalArgumentException("bad descriptor type '" + k + "'");
    }
  }

  /** Drop the package and any enclosing class: "a.b.Outer$Inner[]" and "Outer.Inner[]" -> "Inner[]". */
  private static String simpleName(String t)
  {
    int cut = Math.max(t.lastIndexOf('.'), t.lastIndexOf('$'));
    return cut < 0 ? t : t.substring(cut + 1);
  }
}
