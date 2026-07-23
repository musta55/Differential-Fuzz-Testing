import java.io.File;

import org.jacoco.core.analysis.Analyzer;
import org.jacoco.core.analysis.CoverageBuilder;
import org.jacoco.core.analysis.IClassCoverage;
import org.jacoco.core.analysis.ICounter;
import org.jacoco.core.analysis.IMethodCoverage;
import org.jacoco.core.tools.ExecFileLoader;

/**
 * Per-method branch/line coverage from a jacoco.exec.
 * Usage: CovReport <jacoco.exec> <classesDir> [classSimpleNameFilter] [methodName]
 * Prints e.g.  DiskStorageRefactored.normalizeFileName branch=4/4 line=5/5
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
    for (IClassCoverage c : cb.getClasses()) {
      String simple = c.getName().substring(c.getName().lastIndexOf('/') + 1);
      if (!simple.contains(classFilter)) {
        continue;
      }
      for (IMethodCoverage m : c.getMethods()) {
        if (!methodFilter.isEmpty() && !m.getName().equals(methodFilter)) {
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
}
