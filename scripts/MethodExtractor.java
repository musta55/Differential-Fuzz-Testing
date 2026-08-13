import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseResult;
import com.github.javaparser.ParserConfiguration;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.BodyDeclaration;
import com.github.javaparser.ast.body.CallableDeclaration;
import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.type.Type;
import com.github.javaparser.printer.configuration.DefaultConfigurationOption;
import com.github.javaparser.printer.configuration.DefaultPrinterConfiguration;
import com.github.javaparser.printer.configuration.PrinterConfiguration;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Find the changed methods of an original/refactored source-tree pair, using a real Java AST.
 *
 * <pre>
 *   java -cp &lt;this&gt;:javaparser-core.jar:gson.jar MethodExtractor &lt;origRoot&gt; &lt;refRoot&gt; &lt;out.json&gt;
 * </pre>
 *
 * <h2>Why an AST and not a regex</h2>
 * The previous implementation blanked comments and literals, then brace-matched with a signature
 * regex. It worked, but every accidental resemblance to a method signature was a bug waiting to
 * happen, and several were: an annotation with arguments parsed as a method
 * ({@code @SuppressWarnings("x")} became the method {@code SuppressWarnings}), a field initialised
 * with an anonymous class parsed as a method, nested types needed a brace-depth guard, and blanking
 * literal contents to protect brace-matching also erased them from the change comparison, so a
 * method whose only edit was a returned string was reported as unchanged and never fuzzed
 * (github issue #3). A parser answers all of those by construction rather than by another special
 * case.
 *
 * <h2>Syntactic only, by design</h2>
 * JavaParser is used WITHOUT symbol resolution. These trees routinely reference types that are not
 * on any classpath yet — the whole point of the later prune step is that some pairs do not compile —
 * so anything requiring resolution would fail on exactly the inputs this has to handle. Everything
 * below needs only syntax: which methods exist, what their bodies are, and whether the two sides
 * differ.
 *
 * <h2>What counts as changed</h2>
 * Bodies are compared as ASTs printed without comments, so reformatting and comment edits are not
 * changes but literal edits are. Signatures are compared too: a same-arity parameter-type change
 * used to leave the bodies equal and the method was silently dropped.
 */
public final class MethodExtractor
{
  private MethodExtractor() {}

  /** Types the differential engine can build directly from fuzz bytes (see ObjectFactory). */
  private static final java.util.Set<String> SCALAR = new java.util.HashSet<>(java.util.Arrays.asList(
      "int", "long", "short", "byte", "char", "boolean", "float", "double",
      "Integer", "Long", "Short", "Byte", "Character", "Boolean", "Float", "Double",
      "String",
      "int[]", "long[]", "short[]", "byte[]", "char[]", "boolean[]", "float[]", "double[]"));

  public static void main(String[] args) throws IOException
  {
    if (args.length < 3) {
      System.err.println("usage: MethodExtractor <originalRoot> <refactoredRoot> <out.json>");
      System.exit(2);
    }
    Path origRoot = Paths.get(args[0]).toAbsolutePath().normalize();
    Path refRoot = Paths.get(args[1]).toAbsolutePath().normalize();

    ParserConfiguration cfg = new ParserConfiguration()
        .setLanguageLevel(ParserConfiguration.LanguageLevel.BLEEDING_EDGE)
        .setCharacterEncoding(StandardCharsets.UTF_8);
    JavaParser parser = new JavaParser(cfg);

    List<Map<String, Object>> pairs = new ArrayList<>();
    List<String> problems = new ArrayList<>();

    for (Path ref : javaFiles(refRoot)) {
      Path rel = refRoot.relativize(ref);
      Path orig = origRoot.resolve(rel);
      if (!Files.isRegularFile(orig)) {
        continue; // a brand-new class has no original to diff against
      }
      Optional<CompilationUnit> oCu = parse(parser, orig, problems);
      Optional<CompilationUnit> rCu = parse(parser, ref, problems);
      if (!oCu.isPresent() || !rCu.isPresent()) {
        continue;
      }
      Map<String, Object> pair = comparePair(rel.toString().replace(File.separatorChar, '/'),
          oCu.get(), rCu.get(), problems);
      if (pair != null) {
        pairs.add(pair);
      }
    }

    Map<String, Object> root = new LinkedHashMap<>();
    root.put("originalRoot", origRoot.toString());
    root.put("refactoredRoot", refRoot.toString());
    root.put("pairs", pairs);
    root.put("problems", problems);

    Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
    try (FileWriter w = new FileWriter(args[2])) {
      gson.toJson(root, w);
    }
    System.out.printf("parsed %d file pairs, %d problem(s)%n", pairs.size(), problems.size());
  }

  // ── file walking ────────────────────────────────────────────────────────────

  private static List<Path> javaFiles(Path root) throws IOException
  {
    List<Path> out = new ArrayList<>();
    if (!Files.isDirectory(root)) {
      return out;
    }
    try (java.util.stream.Stream<Path> s = Files.walk(root)) {
      s.filter(Files::isRegularFile)
       .filter(p -> p.getFileName().toString().endsWith(".java"))
       .sorted()
       .forEach(out::add);
    }
    return out;
  }

  private static Optional<CompilationUnit> parse(JavaParser parser, Path p, List<String> problems)
  {
    try {
      ParseResult<CompilationUnit> res = parser.parse(p);
      if (!res.isSuccessful() || !res.getResult().isPresent()) {
        problems.add(p + ": " + res.getProblems());
        return Optional.empty();
      }
      return res.getResult();
    } catch (IOException e) {
      problems.add(p + ": " + e);
      return Optional.empty();
    }
  }

  // ── pairing + diffing ───────────────────────────────────────────────────────

  private static Map<String, Object> comparePair(String rel, CompilationUnit oCu,
      CompilationUnit rCu, List<String> problems)
  {
    String stem = rel.substring(rel.lastIndexOf('/') + 1).replaceAll("\\.java$", "");
    Optional<TypeDeclaration<?>> oPrimary = primaryType(oCu, stem);
    Optional<TypeDeclaration<?>> rPrimary = primaryType(rCu, stem);
    if (!oPrimary.isPresent() || !rPrimary.isPresent()) {
      return null; // no type named after the file: nothing addressable as <Class>.method
    }
    String pkg = rCu.getPackageDeclaration().map(d -> d.getNameAsString()).orElse("");
    if (pkg.isEmpty()) {
      return null; // the snapshot writer needs a package to place the file
    }

    Map<String, CallableDeclaration<?>> oMethods = callables(oPrimary.get(), stem);
    Map<String, CallableDeclaration<?>> rMethods = callables(rPrimary.get(), stem);

    List<Map<String, Object>> methods = new ArrayList<>();
    for (Map.Entry<String, CallableDeclaration<?>> e : rMethods.entrySet()) {
      CallableDeclaration<?> rM = e.getValue();
      CallableDeclaration<?> oM = oMethods.get(e.getKey());
      if (oM == null) {
        continue; // added only: no original to compare against
      }
      String change = changeBetween(oM, rM);
      if (change == null) {
        continue; // unchanged
      }
      methods.add(describe(rM, stem, change));
    }
    if (methods.isEmpty()) {
      return null;
    }

    Map<String, Object> pair = new LinkedHashMap<>();
    pair.put("relPath", rel);
    pair.put("package", pkg);
    pair.put("primaryType", stem);
    pair.put("methods", methods);
    pair.put("secondaryTypes", secondaryNames(rCu, stem));
    return pair;
  }

  private static Optional<TypeDeclaration<?>> primaryType(CompilationUnit cu, String stem)
  {
    for (TypeDeclaration<?> t : cu.getTypes()) {
      if (t.getNameAsString().equals(stem)) {
        return Optional.of(t);
      }
    }
    return Optional.empty();
  }

  private static List<String> secondaryNames(CompilationUnit cu, String stem)
  {
    List<String> out = new ArrayList<>();
    for (TypeDeclaration<?> t : cu.getTypes()) {
      if (!t.getNameAsString().equals(stem)) {
        out.add(t.getNameAsString());
      }
    }
    return out;
  }

  /**
   * Directly-declared methods and constructors of one type, keyed by name/arity.
   *
   * <p>Only members of the primary type: {@code getMembers()} does not descend into nested types,
   * so the old brace-depth guard is unnecessary. Overloads that share an arity get a {@code #n}
   * suffix, matching the manifest ids the rest of the pipeline already uses.
   */
  private static Map<String, CallableDeclaration<?>> callables(TypeDeclaration<?> type, String stem)
  {
    Map<String, CallableDeclaration<?>> out = new LinkedHashMap<>();
    for (BodyDeclaration<?> m : type.getMembers()) {
      CallableDeclaration<?> c;
      if (m instanceof MethodDeclaration) {
        c = (MethodDeclaration) m;
      } else if (m instanceof ConstructorDeclaration) {
        c = (ConstructorDeclaration) m;
      } else {
        continue;
      }
      String base = c.getNameAsString() + "/" + c.getParameters().size();
      String key = base;
      for (int i = 1; out.containsKey(key); i++) {
        key = base + "#" + i;
      }
      out.put(key, c);
    }
    return out;
  }

  /**
   * What differs between the two declarations, or null when they are equivalent.
   *
   * <p>Comment-free printing is the whole comparison: it normalises formatting and ignores comment
   * edits (rewording a javadoc must not mark a method as changed) while preserving literals, which
   * a text-level normaliser could not do without also breaking brace matching.
   */
  private static String changeBetween(CallableDeclaration<?> o, CallableDeclaration<?> r)
  {
    String oSig = signature(o);
    String rSig = signature(r);
    if (!oSig.equals(rSig)) {
      // Same name and arity, different parameter types. The bodies may well be identical, in which
      // case the old body-only comparison called this "unchanged" and dropped the method entirely.
      return "signature: " + oSig + " -> " + rSig;
    }
    String oBody = bodyOf(o);
    String rBody = bodyOf(r);
    if (!oBody.equals(rBody)) {
      return "body";
    }
    return null;
  }

  private static String signature(CallableDeclaration<?> c)
  {
    StringBuilder sb = new StringBuilder(c.getNameAsString()).append('(');
    List<Parameter> ps = c.getParameters();
    for (int i = 0; i < ps.size(); i++) {
      if (i > 0) {
        sb.append(',');
      }
      sb.append(ps.get(i).getType().asString());
      if (ps.get(i).isVarArgs()) {
        sb.append("...");
      }
    }
    sb.append(')');
    if (c instanceof MethodDeclaration) {
      sb.append(':').append(((MethodDeclaration) c).getType().asString());
    }
    return sb.toString();
  }

  private static String bodyOf(CallableDeclaration<?> c)
  {
    Optional<? extends Node> body;
    if (c instanceof MethodDeclaration) {
      body = ((MethodDeclaration) c).getBody();
    } else {
      body = Optional.of(((ConstructorDeclaration) c).getBody());
    }
    if (!body.isPresent()) {
      return "<abstract>";
    }
    return printWithoutComments(body.get());
  }

  private static String printWithoutComments(Node n)
  {
    PrinterConfiguration conf = new DefaultPrinterConfiguration()
        .removeOption(new DefaultConfigurationOption(
            DefaultPrinterConfiguration.ConfigOption.PRINT_COMMENTS));
    return n.toString(conf).replaceAll("\\s+", " ").trim();
  }

  // ── manifest description ────────────────────────────────────────────────────

  private static Map<String, Object> describe(CallableDeclaration<?> c, String stem, String change)
  {
    boolean isCtor = c instanceof ConstructorDeclaration;
    List<String> params = new ArrayList<>();
    boolean allScalar = true;
    for (Parameter p : c.getParameters()) {
      String t = p.getType().asString() + (p.isVarArgs() ? "[]" : "");
      params.add(fqn(t));
      if (!SCALAR.contains(t)) {
        allScalar = false;
      }
    }
    Map<String, Object> m = new LinkedHashMap<>();
    m.put("name", isCtor ? "<init>" : c.getNameAsString());
    m.put("simpleName", c.getNameAsString());
    m.put("ctor", isCtor);
    m.put("static", !isCtor && c.hasModifier(Modifier.Keyword.STATIC));
    m.put("params", params);
    m.put("arity", params.size());
    m.put("kind", isCtor ? "ctor" : (allScalar ? "scalar" : "object"));
    m.put("change", change);
    return m;
  }

  /**
   * Spell a source-level type the way the manifest wants it.
   *
   * <p>Advisory only — the engine resolves methods reflectively and reads the real types off the
   * compiled class — so this only has to be good enough to disambiguate same-arity overloads.
   */
  private static String fqn(String t)
  {
    switch (t) {
      case "String": return "java.lang.String";
      case "Integer": return "java.lang.Integer";
      case "Long": return "java.lang.Long";
      case "Short": return "java.lang.Short";
      case "Byte": return "java.lang.Byte";
      case "Character": return "java.lang.Character";
      case "Boolean": return "java.lang.Boolean";
      case "Float": return "java.lang.Float";
      case "Double": return "java.lang.Double";
      default: return t;
    }
  }
}
