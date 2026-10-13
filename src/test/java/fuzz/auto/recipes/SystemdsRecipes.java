package fuzz.auto.recipes;

import java.util.ArrayList;

import com.code_intelligence.jazzer.api.FuzzedDataProvider;

/**
 * systemds types the generic engine could not build, so every method taking or living on one
 * never ran. All of them are reached through reflection on the side's class loader.
 */
final class SystemdsRecipes
{
  private SystemdsRecipes() {}

  private static Class<?> cls(Class<?> type, String name) throws ClassNotFoundException
  {
    return Class.forName(name, true, type.getClassLoader());
  }

  @SuppressWarnings({"unchecked", "rawtypes"})
  private static Object enumValue(Class<?> type, String enumClass, String constant) throws Exception
  {
    return Enum.valueOf((Class<Enum>) cls(type, enumClass), constant);
  }

  /**
   * Lop: abstract, and every subclass constructor needs other Lops as inputs, so the whole lop
   * hierarchy (Binary, LeftIndex, MapMult, ...) never ran. A literal lop is the leaf systemds itself
   * builds every lop DAG from (Data.createLiteralLop).
   */
  static final class Lop implements Recipe
  {
    @Override
    public Object build(FuzzedDataProvider data, Class<?> type) throws Exception
    {
      String[][] literals = {{"FP64", "1.5"}, {"INT64", "7"}, {"BOOLEAN", "true"}, {"STRING", "abc"}};
      String[] pick = data.pickValue(literals);
      Object vt = enumValue(type, "org.apache.sysds.common.Types$ValueType", pick[0]);
      return cls(type, "org.apache.sysds.lops.Data")
          .getMethod("createLiteralLop", vt.getClass(), String.class).invoke(null, vt, pick[1]);
    }
  }

  /**
   * CSVReBlock: its constructor accepts only the SPARK and OOC execution types, so random ones
   * never built it, and prepCSVProperties reads the CSV options off a read lop. A CSV read of "X"
   * as the parser makes it: each option a literal, left out (its default), or rarely a variable
   * (which the method rejects).
   */
  static final class CSVReBlock implements Recipe
  {
    @Override
    public Object build(FuzzedDataProvider data, Class<?> type) throws Exception
    {
      Class<?> dataLop = cls(type, "org.apache.sysds.lops.Data");
      Object stringVt = enumValue(type, "org.apache.sysds.common.Types$ValueType", "STRING");
      java.lang.reflect.Method literal = dataLop.getMethod("createLiteralLop", stringVt.getClass(), String.class);
      Object read = enumValue(type, "org.apache.sysds.common.Types$OpOpData", "PERSISTENTREAD");
      Object transientRead = enumValue(type, "org.apache.sysds.common.Types$OpOpData", "TRANSIENTREAD");
      Object matrix = enumValue(type, "org.apache.sysds.common.Types$DataType", "MATRIX");
      Object scalar = enumValue(type, "org.apache.sysds.common.Types$DataType", "SCALAR");
      Object fp64 = enumValue(type, "org.apache.sysds.common.Types$ValueType", "FP64");
      Object csv = enumValue(type, "org.apache.sysds.common.Types$FileFormat", "CSV");
      java.lang.reflect.Constructor<?> newData = dataLop.getConstructor(read.getClass(),
          cls(type, "org.apache.sysds.lops.Lop"), java.util.HashMap.class, String.class, String.class,
          matrix.getClass(), fp64.getClass(), csv.getClass());

      String[][] options = {
          {"header", String.valueOf(data.consumeBoolean())},
          {"sep", data.pickValue(new String[] {",", ";", "|", "\t"})},
          {"fill", String.valueOf(data.consumeBoolean())},
          {"default", String.valueOf(data.consumeRegularDouble(-10, 10))},
          {"naStrings", data.pickValue(new String[] {"NA", "", "null"})}};
      java.util.HashMap<String, Object> params = new java.util.HashMap<>();
      for (String[] option : options) {
        int how = data.consumeInt(0, 9); // 0..6 literal, 7..8 left out, 9 a variable
        if (how <= 6) {
          params.put(option[0], literal.invoke(null, stringVt, option[1]));
        } else if (how == 9) {
          params.put(option[0], newData.newInstance(transientRead, null, new java.util.HashMap<>(),
              "v" + option[0], null, scalar, stringVt, null));
        }
      }
      Object input = newData.newInstance(read, null, params, "X", null, matrix, fp64, csv);
      Object exec = enumValue(type, "org.apache.sysds.common.Types$ExecType",
          data.consumeBoolean() ? "SPARK" : "OOC");
      return type.getConstructor(cls(type, "org.apache.sysds.lops.Lop"), int.class, matrix.getClass(),
          fp64.getClass(), exec.getClass()).newInstance(input, data.consumeInt(1, 2000), matrix, fp64, exec);
    }
  }

  /**
   * IterablePredicate (a for / parfor header): its constructor reads line numbers off an ANTLR
   * parse context, which the engine could not make. A context with start/stop tokens, a loop
   * variable "i" and integer bounds, as the parser builds it.
   */
  static final class IterablePredicate implements Recipe
  {
    @Override
    public Object build(FuzzedDataProvider data, Class<?> type) throws Exception
    {
      Class<?> ctxClass = cls(type, "org.antlr.v4.runtime.ParserRuleContext");
      Class<?> token = cls(type, "org.antlr.v4.runtime.Token");
      Object ctx = ctxClass.getConstructor().newInstance();
      Object start = cls(type, "org.antlr.v4.runtime.CommonToken").getConstructor(int.class, String.class)
          .newInstance(1, "for");
      ctxClass.getField("start").set(ctx, start);
      ctxClass.getField("stop").set(ctx, start);
      Class<?> intId = cls(type, "org.apache.sysds.parser.IntIdentifier");
      java.lang.reflect.Constructor<?> literal = intId.getConstructor(long.class);
      java.util.HashMap<String, String> parfor = data.consumeBoolean() ? null : new java.util.HashMap<String, String>();
      return type.getConstructor(ctxClass, cls(type, "org.apache.sysds.parser.DataIdentifier"),
          cls(type, "org.apache.sysds.parser.Expression"), cls(type, "org.apache.sysds.parser.Expression"),
          cls(type, "org.apache.sysds.parser.Expression"), java.util.HashMap.class, String.class)
          .newInstance(ctx, cls(type, "org.apache.sysds.parser.DataIdentifier").getConstructor(String.class)
              .newInstance("i"), literal.newInstance(data.consumeLong(-5, 20)),
              literal.newInstance(data.consumeLong(-5, 20)),
              data.consumeBoolean() ? null : literal.newInstance(data.consumeLong(-3, 3)), parfor, "script");
    }
  }

  /** Expression, for IterablePredicate.checkNumericScalarOutput: a typed identifier or an integer. */
  static final class Expression implements Recipe
  {
    @Override
    public Object build(FuzzedDataProvider data, Class<?> type) throws Exception
    {
      if (data.consumeBoolean()) {
        return identifier(data, type);
      }
      Object literal = cls(type, "org.apache.sysds.parser.IntIdentifier").getConstructor(long.class)
          .newInstance(data.consumeLong(-5, 20));
      setOutput(type, literal, literal);
      return literal;
    }
  }

  /** AIdentityDictionary: abstract; systemds' identity dictionary of a fuzzed size. */
  static final class IdentityDictionary implements Recipe
  {
    @Override
    public Object build(FuzzedDataProvider data, Class<?> type) throws Exception
    {
      return cls(type, "org.apache.sysds.runtime.compress.colgroup.dictionary.IdentityDictionary")
          .getMethod("create", int.class, boolean.class).invoke(null, data.consumeInt(2, 8), data.consumeBoolean());
    }
  }

  /** ACLAScheme: abstract; a constant scheme or a DDC scheme over a few columns. */
  static final class Scheme implements Recipe
  {
    @Override
    public Object build(FuzzedDataProvider data, Class<?> type) throws Exception
    {
      if (data.consumeBoolean()) {
        return new ConstScheme().build(data, cls(type, "org.apache.sysds.runtime.compress.colgroup.scheme.ConstScheme"));
      }
      Class<?> iColIndex = cls(type, "org.apache.sysds.runtime.compress.colgroup.indexes.IColIndex");
      return cls(type, "org.apache.sysds.runtime.compress.colgroup.scheme.DDCScheme")
          .getMethod("create", iColIndex).invoke(null, new IColIndex().build(data, iColIndex));
    }
  }

  /**
   * AColGroupOffset (OLE / RLE column groups): their constructors are private; systemds makes them
   * by compressing matrix columns. A small matrix, its bitmap, then the OLE or RLE compression.
   */
  static final class ColGroupOffset implements Recipe
  {
    @Override
    public Object build(FuzzedDataProvider data, Class<?> type) throws Exception
    {
      Object m = AComEst.matrix(data, type);
      int rows = (Integer) m.getClass().getMethod("getNumRows").invoke(m);
      int cols = (Integer) m.getClass().getMethod("getNumColumns").invoke(m);
      int[] picked = new int[data.consumeInt(1, cols)];
      java.lang.reflect.Method get = m.getClass().getMethod("get", int.class, int.class);
      java.lang.reflect.Method set = m.getClass().getMethod("set", int.class, int.class, double.class);
      for (int i = 0; i < picked.length; i++) {
        picked[i] = i;
        // an all-zero column has nothing to encode: extractBitmap returns null and compression
        // fails, which is what most inputs gave (out of bytes, every value is 0)
        if ((Double) get.invoke(m, 0, i) == 0) {
          set.invoke(m, 0, i, (double) data.consumeInt(1, 3));
        }
      }
      Class<?> iColIndex = cls(type, "org.apache.sysds.runtime.compress.colgroup.indexes.IColIndex");
      Object index = cls(type, "org.apache.sysds.runtime.compress.colgroup.indexes.ColIndexFactory")
          .getMethod("create", int[].class).invoke(null, (Object) picked);
      Object bitmap = cls(type, "org.apache.sysds.runtime.compress.bitmap.BitmapEncoder")
          .getMethod("extractBitmap", iColIndex, m.getClass(), boolean.class, int.class, boolean.class)
          .invoke(null, index, m, false, 8, false);
      String group = data.consumeBoolean() ? "RLE" : "OLE";
      java.lang.reflect.Method compress = cls(type, "org.apache.sysds.runtime.compress.colgroup.ColGroup" + group)
          .getDeclaredMethod("compress" + group, iColIndex,
              cls(type, "org.apache.sysds.runtime.compress.bitmap.ABitmap"), int.class, double.class);
      compress.setAccessible(true);
      return compress.invoke(null, index, bitmap, rows, 1.0);
    }
  }

  /** ConstScheme: its constructor is private; its factory, with one value per column. */
  static final class ConstScheme implements Recipe
  {
    @Override
    public Object build(FuzzedDataProvider data, Class<?> type) throws Exception
    {
      int n = data.consumeInt(1, 4);
      int[] cols = new int[n];
      double[] vals = new double[n];
      for (int i = 0; i < n; i++) {
        cols[i] = i;
        vals[i] = data.consumeInt(-3, 3);
      }
      Object index = cls(type, "org.apache.sysds.runtime.compress.colgroup.indexes.ColIndexFactory")
          .getMethod("create", int[].class).invoke(null, (Object) cols);
      return type.getMethod("create", cls(type, "org.apache.sysds.runtime.compress.colgroup.indexes.IColIndex"),
          double[].class).invoke(null, index, vals);
    }
  }

  /**
   * A {row, column} index into a small dense block, each 0 or 1. DenseBlock.set(int[], String) and
   * getLong(int[]) failed on almost every random index (wrong length, out of range), so their
   * bodies never ran.
   */
  static final class CellIndex implements Recipe
  {
    @Override
    public Object build(FuzzedDataProvider data, Class<?> type)
    {
      return new int[] {data.consumeInt(0, 1), data.consumeInt(0, 1)};
    }
  }

  /** A row or column number of a small dense block: 0 or 1. */
  static final class CellNumber implements Recipe
  {
    @Override
    public Object build(FuzzedDataProvider data, Class<?> type)
    {
      return data.consumeInt(0, 1);
    }
  }

  /** A number as text, whole or not, for DenseBlock.set(int[], String), which parses it. */
  static final class NumberText implements Recipe
  {
    @Override
    public Object build(FuzzedDataProvider data, Class<?> type)
    {
      return data.consumeBoolean() ? String.valueOf(data.consumeInt(-1000, 1000))
          : String.valueOf(data.consumeRegularDouble(-1000, 1000));
    }
  }

  /**
   * A DenseBlockLString of 1..3 x 1..3 cells holding whole numbers as text, so getLong can parse
   * them. The cells are written straight into its block (one block at this size), not through
   * set(...), which is itself one of the refactored methods.
   */
  static final class DenseBlockLString implements Recipe
  {
    @Override
    public Object build(FuzzedDataProvider data, Class<?> type) throws Exception
    {
      Object block = type.getConstructor(int[].class)
          .newInstance((Object) new int[] {data.consumeInt(1, 3), data.consumeInt(1, 3)});
      java.lang.reflect.Field blocks = type.getDeclaredField("_blocks");
      blocks.setAccessible(true);
      String[] cells = ((String[][]) blocks.get(block))[0];
      for (int i = 0; i < cells.length; i++) {
        cells[i] = String.valueOf(data.consumeInt(-100, 100));
      }
      return block;
    }
  }

  /** A DenseBlockLFP64DEDUP of 1..3 x 1..3 cells; its constructor is protected. */
  static final class DenseBlockLFP64DEDUP implements Recipe
  {
    @Override
    public Object build(FuzzedDataProvider data, Class<?> type) throws Exception
    {
      java.lang.reflect.Constructor<?> ctor = type.getDeclaredConstructor(int[].class);
      ctor.setAccessible(true);
      return ctor.newInstance((Object) new int[] {data.consumeInt(1, 3), data.consumeInt(1, 3)});
    }
  }

  /**
   * CP instructions whose constructors are private: systemds makes them only by parsing their
   * text. One of a few instruction texts reading the variables A, B, W (matrices) and q (scalar),
   * which {@link InstructionContext} puts in the execution context.
   */
  static final class CPInstruction implements Recipe
  {
    private final String[] texts;

    CPInstruction(String... texts)
    {
      this.texts = texts;
    }

    @Override
    public Object build(FuzzedDataProvider data, Class<?> type) throws Exception
    {
      return type.getMethod("parseInstruction", String.class).invoke(null, data.pickValue(texts));
    }
  }

  /**
   * The ExecutionContext a {@link CPInstruction} runs in: column vectors A, B (same length) and W
   * (positive weights), A sorted (qpick expects sorted input), a scalar q in [0, 1], frames F and
   * G (one text column each), and the outputs out (matrix) and fout (frame). Each matrix and frame
   * gets a fixed file name, so both sides hold equal contexts.
   */
  static final class InstructionContext implements Recipe
  {
    @Override
    public Object build(FuzzedDataProvider data, Class<?> type) throws Exception
    {
      Object ec = cls(type, "org.apache.sysds.runtime.controlprogram.context.ExecutionContextFactory")
          .getMethod("createContext").invoke(null);
      Class<?> dataClass = cls(type, "org.apache.sysds.runtime.instructions.cp.Data");
      java.lang.reflect.Method setVariable = ec.getClass().getMethod("setVariable", String.class, dataClass);
      int rows = data.consumeInt(1, 16);
      double[] a = values(data, rows, -5, 5);
      java.util.Arrays.sort(a);
      setVariable.invoke(ec, "A", matrixObject(type, "A", a));
      setVariable.invoke(ec, "B", matrixObject(type, "B", values(data, rows, -5, 5)));
      setVariable.invoke(ec, "W", matrixObject(type, "W", values(data, rows, 1, 3)));
      setVariable.invoke(ec, "q", cls(type, "org.apache.sysds.runtime.instructions.cp.DoubleObject")
          .getConstructor(double.class).newInstance(data.consumeRegularDouble(0, 1)));
      // matrix outputs must exist before an instruction writes them (systemds creates them first)
      setVariable.invoke(ec, "out", matrixObject(type, "out", new double[] {0}));
      // frames F and G: one column of numbers as text, as many rows as A
      setVariable.invoke(ec, "F", frameObject(type, "F", values(data, rows, -5, 5)));
      setVariable.invoke(ec, "G", frameObject(type, "G", values(data, rows, -5, 5)));
      setVariable.invoke(ec, "fout", frameObject(type, "fout", new double[] {0}));
      return ec;
    }

    /** A one-column frame of these values as text, held in memory under a fixed file name. */
    private static Object frameObject(Class<?> type, String name, double[] v) throws Exception
    {
      Class<?> vt = cls(type, "org.apache.sysds.common.Types$ValueType");
      Object[] schema = (Object[]) java.lang.reflect.Array.newInstance(vt, 1);
      schema[0] = enumValue(type, vt.getName(), "STRING");
      String[][] rows = new String[v.length][1];
      for (int i = 0; i < v.length; i++) {
        rows[i][0] = String.valueOf(v[i]);
      }
      Object block = cls(type, "org.apache.sysds.runtime.frame.data.FrameBlock")
          .getConstructor(schema.getClass(), String[][].class).newInstance(schema, rows);
      Object mc = cls(type, "org.apache.sysds.runtime.meta.MatrixCharacteristics")
          .getConstructor(long.class, long.class, int.class).newInstance((long) v.length, 1L, 1000);
      Object binary = enumValue(type, "org.apache.sysds.common.Types$FileFormat", "BINARY");
      Object md = cls(type, "org.apache.sysds.runtime.meta.MetaDataFormat")
          .getConstructor(cls(type, "org.apache.sysds.runtime.meta.DataCharacteristics"), binary.getClass())
          .newInstance(mc, binary);
      Class<?> fo = cls(type, "org.apache.sysds.runtime.controlprogram.caching.FrameObject");
      Object frame = fo.getConstructor(String.class, cls(type, "org.apache.sysds.runtime.meta.MetaData"))
          .newInstance("scratch/" + name, md);
      fo.getMethod("acquireModify", cls(type, "org.apache.sysds.runtime.controlprogram.caching.CacheBlock"))
          .invoke(frame, block);
      fo.getMethod("release").invoke(frame);
      return frame;
    }

    private static double[] values(FuzzedDataProvider data, int n, int min, int max)
    {
      double[] v = new double[n];
      for (int i = 0; i < n; i++) {
        v[i] = data.consumeInt(min, max);
      }
      return v;
    }

    /** A column vector held in memory, under a fixed file name. */
    private static Object matrixObject(Class<?> type, String name, double[] v) throws Exception
    {
      Class<?> mb = cls(type, "org.apache.sysds.runtime.matrix.data.MatrixBlock");
      Object block = mb.getConstructor(int.class, int.class, boolean.class).newInstance(v.length, 1, false);
      java.lang.reflect.Method set = mb.getMethod("set", int.class, int.class, double.class);
      for (int i = 0; i < v.length; i++) {
        set.invoke(block, i, 0, v[i]);
      }
      Object mc = cls(type, "org.apache.sysds.runtime.meta.MatrixCharacteristics")
          .getConstructor(long.class, long.class, int.class).newInstance((long) v.length, 1L, 1000);
      Object binary = enumValue(type, "org.apache.sysds.common.Types$FileFormat", "BINARY");
      Object md = cls(type, "org.apache.sysds.runtime.meta.MetaDataFormat")
          .getConstructor(cls(type, "org.apache.sysds.runtime.meta.DataCharacteristics"), binary.getClass())
          .newInstance(mc, binary);
      Object fp64 = enumValue(type, "org.apache.sysds.common.Types$ValueType", "FP64");
      return cls(type, "org.apache.sysds.runtime.controlprogram.caching.MatrixObject")
          .getConstructor(fp64.getClass(), String.class, cls(type, "org.apache.sysds.runtime.meta.MetaData"), mb)
          .newInstance(fp64, "scratch/" + name, md, block);
    }
  }

  /** netty HttpRequest, for the federated-monitoring Request.setContext: a GET on a fuzzed path. */
  static final class HttpRequest implements Recipe
  {
    @Override
    public Object build(FuzzedDataProvider data, Class<?> type) throws Exception
    {
      Class<?> version = cls(type, "io.netty.handler.codec.http.HttpVersion");
      Class<?> method = cls(type, "io.netty.handler.codec.http.HttpMethod");
      String path = "/" + data.consumeAsciiString(8).replaceAll("[^A-Za-z0-9]", "");
      return cls(type, "io.netty.handler.codec.http.DefaultFullHttpRequest")
          .getConstructor(version, method, String.class)
          .newInstance(version.getField("HTTP_1_1").get(null), method.getField("GET").get(null), path);
    }
  }

  /**
   * LopProperties: each one takes the next number of a global counter (UniqueLopID) and toString()
   * prints it, so the self-check's rebuild of the same input printed "ID: 1" where the first run had
   * "ID: 0" and was reported nondeterministic. The counter is reset before building, so the same
   * input builds the same object; level, exec type and the intermediate flag are fuzzed.
   */
  static final class LopProperties implements Recipe
  {
    @Override
    public Object build(FuzzedDataProvider data, Class<?> type) throws Exception
    {
      java.lang.reflect.Field counter = type.getDeclaredField("UniqueLopID");
      counter.setAccessible(true);
      Object sequence = counter.get(null);
      sequence.getClass().getMethod("reset").invoke(sequence);
      Object properties = type.getConstructor().newInstance();
      type.getMethod("setLevel", int.class).invoke(properties, data.consumeInt(0, 5));
      Object execType = enumValue(type, "org.apache.sysds.common.Types$ExecType",
          data.pickValue(new String[] {"CP", "SPARK", "GPU", "INVALID"}));
      type.getMethod("setExecType", execType.getClass()).invoke(properties, execType);
      type.getMethod("setProducesIntermediateOutput", boolean.class).invoke(properties, data.consumeBoolean());
      return properties;
    }
  }

  /**
   * The ANTLR parse context the DML parser passes to expressions and statements: they read the
   * line and column of its start and stop tokens. Built empty, both tokens were null and every
   * call failed before setting anything. Two tokens at small fuzzed positions, stop after start.
   */
  static final class ParserRuleContext implements Recipe
  {
    @Override
    public Object build(FuzzedDataProvider data, Class<?> type) throws Exception
    {
      Object context = type.getConstructor().newInstance();
      int line = data.consumeInt(1, 50);
      int column = data.consumeInt(0, 40);
      type.getField("start").set(context, token(type, line, column));
      type.getField("stop").set(context, token(type, line + data.consumeInt(0, 3), column + data.consumeInt(0, 20)));
      return context;
    }

    private static Object token(Class<?> type, int line, int column) throws Exception
    {
      Class<?> tokenClass = cls(type, "org.antlr.v4.runtime.CommonToken");
      Object token = tokenClass.getConstructor(int.class, String.class).newInstance(1, "x");
      tokenClass.getMethod("setLine", int.class).invoke(token, line);
      tokenClass.getMethod("setCharPositionInLine", int.class).invoke(token, column);
      return token;
    }
  }

  /** CNode / CNodeData: a named data node, the leaf of every codegen plan. */
  static final class CNodeData implements Recipe
  {
    @Override
    public Object build(FuzzedDataProvider data, Class<?> type) throws Exception
    {
      resetVarCounter(type);
      return node(data, type);
    }

    /**
     * CNode names the variables of generated code from one global counter (TMP0, TMP1, ...), so
     * the self-check's rerun of the same input generated TMP2 where the first run had TMP0, and
     * was reported nondeterministic. Every node is built right before its call: starting the
     * counter from 0 here makes the same input generate the same code.
     */
    static void resetVarCounter(Class<?> type) throws Exception
    {
      java.lang.reflect.Field counter = cls(type, "org.apache.sysds.hops.codegen.cplan.CNode").getDeclaredField("_seqVar");
      counter.setAccessible(true);
      Object sequence = counter.get(null);
      sequence.getClass().getMethod("reset").invoke(sequence);
    }

    static Object node(FuzzedDataProvider data, Class<?> type) throws Exception
    {
      Object dt = enumValue(type, "org.apache.sysds.common.Types$DataType",
          data.consumeBoolean() ? "MATRIX" : "SCALAR");
      return cls(type, "org.apache.sysds.hops.codegen.cplan.CNodeData")
          .getConstructor(String.class, long.class, long.class, long.class, dt.getClass())
          .newInstance(data.pickValue(new String[] {"A", "B", "X", "1.0"}), data.consumeLong(0, 99),
              data.consumeLong(1, 16), data.consumeLong(1, 16), dt);
    }

    static ArrayList<Object> nodes(FuzzedDataProvider data, Class<?> type, int min) throws Exception
    {
      ArrayList<Object> list = new ArrayList<>();
      int n = data.consumeInt(min, 3);
      for (int i = 0; i < n; i++) {
        list.add(node(data, type));
      }
      return list;
    }
  }

  /**
   * CNodeTpl / CNodeMultiAgg: the template constructor rejects an empty input list, which is what
   * the generic engine kept passing. Built from one to three data nodes, with one aggregation per
   * output: the constructor leaves the aggregations null and systemds sets them right after, so
   * without them every method reading them (getTemplateInfo ...) failed on its first line.
   */
  static final class CNodeMultiAgg implements Recipe
  {
    @Override
    public Object build(FuzzedDataProvider data, Class<?> type) throws Exception
    {
      CNodeData.resetVarCounter(type);
      ArrayList<Object> outputs = CNodeData.nodes(data, type, 1);
      Class<?> c = cls(type, "org.apache.sysds.hops.codegen.cplan.CNodeMultiAgg");
      Object node = c.getConstructor(ArrayList.class, ArrayList.class)
          .newInstance(CNodeData.nodes(data, type, 1), outputs);
      ArrayList<Object> aggOps = new ArrayList<>();
      for (int i = 0; i < outputs.size(); i++) {
        aggOps.add(enumValue(type, "org.apache.sysds.common.Types$AggOp",
            data.pickValue(new String[] {"SUM", "SUM_SQ", "MIN", "MAX"})));
      }
      c.getMethod("setAggOps", ArrayList.class).invoke(node, aggOps);
      return node;
    }
  }

  /** CNodeOuterProduct: as above; a self-transpose type (not NONE) needs at least two inputs. */
  static final class CNodeOuterProduct implements Recipe
  {
    @Override
    public Object build(FuzzedDataProvider data, Class<?> type) throws Exception
    {
      Object mmtsj = enumValue(type, "org.apache.sysds.lops.MMTSJ$MMTSJType",
          data.pickValue(new String[] {"NONE", "LEFT", "RIGHT"}));
      Class<?> cnode = cls(type, "org.apache.sysds.hops.codegen.cplan.CNode");
      return cls(type, "org.apache.sysds.hops.codegen.cplan.CNodeOuterProduct")
          .getConstructor(ArrayList.class, cnode, mmtsj.getClass())
          .newInstance(CNodeData.nodes(data, type, 2), CNodeData.node(data, type), mmtsj);
    }
  }

  /**
   * DenseBlock (any concrete one, and the abstract DenseBlock / DRB / LDRB): the constructors
   * allocate dims[0] x dims[1] cells, and fuzzed dimensions asked for gigabytes (DenseBlockLBool ran
   * out of heap). Small dimensions, filled with one fuzzed value.
   */
  static final class DenseBlock implements Recipe
  {
    private final String concrete; // null: build the requested class itself

    DenseBlock(String concrete)
    {
      this.concrete = concrete;
    }

    @Override
    public Object build(FuzzedDataProvider data, Class<?> type) throws Exception
    {
      Class<?> c = concrete == null ? type : cls(type, "org.apache.sysds.runtime.data." + concrete);
      int[] dims = {data.consumeInt(1, 16), data.consumeInt(1, 16)};
      Object block = c.getConstructor(int[].class).newInstance((Object) dims);
      try {
        c.getMethod("set", double.class).invoke(block, data.consumeDouble());
      } catch (Exception e) {
        // a block that cannot take a double (string blocks) stays at its default
      }
      return block;
    }
  }

  /** IndexFunction / ValueFunction / FunctionObject: abstract, with only singleton factories. */
  static final class FunctionObject implements Recipe
  {
    private static final String[][] INDEX = {
        {"SwapIndex", "getSwapIndexFnObject"}, {"ReduceAll", "getReduceAllFnObject"},
        {"ReduceCol", "getReduceColFnObject"}, {"ReduceRow", "getReduceRowFnObject"}};
    private static final String[][] VALUE = {
        {"Plus", "getPlusFnObject"}, {"Minus", "getMinusFnObject"},
        {"Multiply", "getMultiplyFnObject"}, {"Divide", "getDivideFnObject"}};

    @Override
    public Object build(FuzzedDataProvider data, Class<?> type) throws Exception
    {
      String name = type.getSimpleName();
      boolean index = name.equals("IndexFunction") || (name.equals("FunctionObject") && data.consumeBoolean());
      return singleton(data, type, index ? INDEX : VALUE);
    }

    static Object valueFunction(FuzzedDataProvider data, Class<?> type) throws Exception
    {
      return singleton(data, type, VALUE);
    }

    private static Object singleton(FuzzedDataProvider data, Class<?> type, String[][] options) throws Exception
    {
      String[] pick = data.pickValue(options);
      return cls(type, "org.apache.sysds.runtime.functionobjects." + pick[0]).getMethod(pick[1]).invoke(null);
    }
  }

  /** ScalarOperator: abstract; a right-scalar operator over one of the value functions above. */
  static final class ScalarOperator implements Recipe
  {
    @Override
    public Object build(FuzzedDataProvider data, Class<?> type) throws Exception
    {
      Class<?> vf = cls(type, "org.apache.sysds.runtime.functionobjects.ValueFunction");
      return cls(type, "org.apache.sysds.runtime.matrix.operators.RightScalarOperator")
          .getConstructor(vf, double.class)
          .newInstance(FunctionObject.valueFunction(data, type), data.consumeDouble());
    }
  }

  /**
   * CompressionSettings: only made through its builder, which draws a random seed; a fixed seed
   * keeps the two sides' settings equal.
   */
  static final class CompressionSettings implements Recipe
  {
    @Override
    public Object build(FuzzedDataProvider data, Class<?> type) throws Exception
    {
      Class<?> builder = cls(type, "org.apache.sysds.runtime.compress.CompressionSettingsBuilder");
      Object b = builder.getConstructor().newInstance();
      builder.getMethod("setSeed", int.class).invoke(b, data.consumeInt(0, 1000));
      return builder.getMethod("create").invoke(b);
    }
  }

  /**
   * DataCharacteristics: abstract with only a few subtypes, so finding one loaded every class under
   * org.apache (hadoop, spark: 63k classes) and the run timed out before a single call. A matrix's
   * dimensions, block size and non-zero count, as systemds describes every matrix.
   */
  static final class DataCharacteristics implements Recipe
  {
    @Override
    public Object build(FuzzedDataProvider data, Class<?> type) throws Exception
    {
      return matrix(data, type);
    }

    static Object matrix(FuzzedDataProvider data, Class<?> type) throws Exception
    {
      long rows = data.consumeLong(1, 1000);
      long cols = data.consumeLong(1, 1000);
      return cls(type, "org.apache.sysds.runtime.meta.MatrixCharacteristics")
          .getConstructor(long.class, long.class, int.class, long.class)
          .newInstance(rows, cols, 1000, data.consumeLong(0, rows * cols));
    }
  }

  /** mlcontext Metadata: abstract with two subtypes, the same scan as above. A matrix's metadata. */
  static final class Metadata implements Recipe
  {
    @Override
    public Object build(FuzzedDataProvider data, Class<?> type) throws Exception
    {
      Class<?> dc = cls(type, "org.apache.sysds.runtime.meta.DataCharacteristics");
      return cls(type, "org.apache.sysds.api.mlcontext.MatrixMetadata").getConstructor(dc)
          .newInstance(DataCharacteristics.matrix(data, type));
    }
  }

  /** SparseRow: abstract with two subtypes, the same scan as above. A row of up to 8 non-zeros. */
  static final class SparseRow implements Recipe
  {
    @Override
    public Object build(FuzzedDataProvider data, Class<?> type) throws Exception
    {
      Class<?> c = cls(type, "org.apache.sysds.runtime.data.SparseRowVector");
      Object row = c.getConstructor(int.class).newInstance(8);
      java.lang.reflect.Method append = c.getMethod("append", int.class, double.class);
      int n = data.consumeInt(0, 8);
      int col = 0;
      for (int i = 0; i < n; i++) {
        col += data.consumeInt(1, 4); // column indexes must increase
        append.invoke(row, col, data.consumeDouble());
      }
      return row;
    }
  }

  /**
   * Types.FunctionBlock: an interface with two implementations, the same scan as above. An empty
   * function statement block, as the parser creates before filling it.
   */
  static final class FunctionBlock implements Recipe
  {
    @Override
    public Object build(FuzzedDataProvider data, Class<?> type) throws Exception
    {
      return cls(type, "org.apache.sysds.parser.FunctionStatementBlock").getConstructor().newInstance();
    }
  }

  /**
   * IIterate, the iterator over a column index: its implementations are private inner classes of
   * the index types, so nothing could build one. The iterator of an index from the IColIndex recipe.
   */
  static final class IIterate implements Recipe
  {
    @Override
    public Object build(FuzzedDataProvider data, Class<?> type) throws Exception
    {
      Class<?> index = cls(type, "org.apache.sysds.runtime.compress.colgroup.indexes.IColIndex");
      return index.getMethod("iterator").invoke(new IColIndex().build(data, index));
    }
  }

  /**
   * IColIndex: autofuzz scanned the whole class path for its implementations on every input. Up to
   * eight increasing column indexes, through systemds' own factory.
   */
  static final class IColIndex implements Recipe
  {
    @Override
    public Object build(FuzzedDataProvider data, Class<?> type) throws Exception
    {
      int[] cols = new int[data.consumeInt(1, 8)];
      int col = -1;
      for (int i = 0; i < cols.length; i++) {
        col += data.consumeInt(1, 4); // must be sorted and distinct
        cols[i] = col;
      }
      return cls(type, "org.apache.sysds.runtime.compress.colgroup.indexes.ColIndexFactory")
          .getMethod("create", int[].class).invoke(null, (Object) cols);
    }
  }

  /**
   * FrameBlock: autofuzz went through its column-array constructor and scanned the class path for
   * the abstract column type. One to three typed columns, zero to four rows.
   */
  static final class FrameBlock implements Recipe
  {
    @Override
    public Object build(FuzzedDataProvider data, Class<?> type) throws Exception
    {
      Object[] schema = schema(data, type, data.consumeInt(1, 3));
      String[][] rows = new String[data.consumeInt(0, 4)][schema.length];
      for (String[] row : rows) {
        for (int j = 0; j < schema.length; j++) {
          row[j] = value(data, schema[j].toString());
        }
      }
      return cls(type, "org.apache.sysds.runtime.frame.data.FrameBlock")
          .getConstructor(schema.getClass(), String[][].class).newInstance(schema, rows);
    }

    static Object[] schema(FuzzedDataProvider data, Class<?> type, int n) throws Exception
    {
      Class<?> vt = cls(type, "org.apache.sysds.common.Types$ValueType");
      Object[] schema = (Object[]) java.lang.reflect.Array.newInstance(vt, n);
      for (int j = 0; j < n; j++) {
        schema[j] = enumValue(type, vt.getName(),
            data.pickValue(new String[] {"STRING", "FP64", "INT64", "BOOLEAN"}));
      }
      return schema;
    }

    private static String value(FuzzedDataProvider data, String valueType)
    {
      switch (valueType) {
        case "FP64": return String.valueOf(data.consumeDouble());
        case "INT64": return String.valueOf(data.consumeLong());
        case "BOOLEAN": return String.valueOf(data.consumeBoolean());
        default: return data.consumeString(8);
      }
    }
  }

  /**
   * FrameReblockBuffer: its constructor creates one cell per buffer slot, and a fuzzed buffer size
   * asked for billions. A small buffer over a frame of up to 16 columns.
   */
  static final class FrameReblockBuffer implements Recipe
  {
    @Override
    public Object build(FuzzedDataProvider data, Class<?> type) throws Exception
    {
      Object[] schema = FrameBlock.schema(data, type, data.consumeInt(1, 16));
      return type.getConstructor(int.class, long.class, long.class, schema.getClass())
          .newInstance(data.consumeInt(1, 64), data.consumeLong(1, 1000), (long) schema.length, schema);
    }
  }

  /**
   * MemorizerV2 keeps one bucket per column: mem = new Map[nCol]. Built with nCol 0, every put /
   * getOrCreate failed on its first array access; and a bucket is only created by put, so
   * getOrCreate failed on any column nothing was put for. 4..8 columns (more than the IColIndex
   * recipe's 0..3), an estimator from the AComEst recipe, and every bucket made, some holding the
   * estimate for their single column, so getOrCreate can find two parts and combine them. The
   * buckets are filled directly, not through put, which is itself one of the refactored methods.
   */
  static final class MemorizerV2 implements Recipe
  {
    @Override
    public Object build(FuzzedDataProvider data, Class<?> type) throws Exception
    {
      Class<?> estimatorClass = cls(type, "org.apache.sysds.runtime.compress.estim.AComEst");
      Class<?> indexClass = cls(type, "org.apache.sysds.runtime.compress.colgroup.indexes.IColIndex");
      Object estimator = new AComEst().build(data, estimatorClass);
      int nCol = data.consumeInt(4, 8);
      Object memorizer = type.getConstructor(estimatorClass, int.class).newInstance(estimator, nCol);
      java.lang.reflect.Field memField = type.getDeclaredField("mem");
      memField.setAccessible(true);
      Object[] mem = (Object[]) memField.get(memorizer);
      java.lang.reflect.Constructor<?> key =
          cls(type, "org.apache.sysds.runtime.compress.cocode.ColIndexes").getConstructor(indexClass);
      for (int col = 0; col < nCol; col++) {
        java.util.Map<Object, Object> bucket = new java.util.HashMap<>();
        mem[col] = bucket;
        if (data.consumeBoolean()) {
          Object index = cls(type, "org.apache.sysds.runtime.compress.colgroup.indexes.ColIndexFactory")
              .getMethod("create", int[].class).invoke(null, (Object) new int[] {col});
          try {
            bucket.put(key.newInstance(index),
                estimatorClass.getMethod("getColGroupInfo", indexClass).invoke(estimator, index));
          } catch (java.lang.reflect.InvocationTargetException e) {
            // a column the estimator's matrix does not have: leave the bucket empty
          }
        }
      }
      return memorizer;
    }
  }

  /**
   * ComEstExact over a small matrix (AComEst.matrix) and fuzzed settings. The estimator built for
   * the current input is remembered, so ColGroupInfo can make column infos from the same matrix:
   * combine(...) merges the two infos' maps, which only works for maps of one matrix.
   */
  static final class ComEstExact implements Recipe
  {
    private static Object last;
    private static Object lastFor;

    @Override
    public Object build(FuzzedDataProvider data, Class<?> type) throws Exception
    {
      Object m = AComEst.matrix(data, type);
      Object cs = new CompressionSettings().build(data, type);
      Object estimator = type.getConstructor(m.getClass(), cs.getClass()).newInstance(m, cs);
      synchronized (ComEstExact.class) {
        last = estimator;
        lastFor = data;
      }
      return estimator;
    }

    /** The estimator built for this input, or null. */
    static synchronized Object of(FuzzedDataProvider data)
    {
      return lastFor == data ? last : null;
    }
  }

  /**
   * A column group's size info with its value map, as the estimator makes it for one of its own
   * columns. Built by autofuzz, the info had no map, so combine(...) failed on its first line.
   */
  static final class ColGroupInfo implements Recipe
  {
    @Override
    public Object build(FuzzedDataProvider data, Class<?> type) throws Exception
    {
      Object estimator = ComEstExact.of(data);
      if (estimator == null) {
        throw new IllegalStateException("no estimator built for this input"); // build as usual
      }
      int cols = (Integer) estimator.getClass().getMethod("getNumColumns").invoke(estimator);
      Class<?> indexClass = cls(type, "org.apache.sysds.runtime.compress.colgroup.indexes.IColIndex");
      Object index = cls(type, "org.apache.sysds.runtime.compress.colgroup.indexes.ColIndexFactory")
          .getMethod("create", int[].class).invoke(null, (Object) new int[] {data.consumeInt(0, cols - 1)});
      return estimator.getClass().getMethod("getColGroupInfo", indexClass).invoke(estimator, index);
    }
  }

  /**
   * A compressed one-column matrix, for central moments: checkCMOperations rejects anything but a
   * single column, and the built block had none. 1..16 rows of one fuzzed value (0 makes it empty),
   * in one or two constant column groups, overlapping or not, so each branch is reachable.
   */
  static final class CompressedColumn implements Recipe
  {
    @Override
    public Object build(FuzzedDataProvider data, Class<?> type) throws Exception
    {
      Class<?> indexClass = cls(type, "org.apache.sysds.runtime.compress.colgroup.indexes.IColIndex");
      Object column = cls(type, "org.apache.sysds.runtime.compress.colgroup.indexes.ColIndexFactory")
          .getMethod("create", int[].class).invoke(null, (Object) new int[] {0});
      java.lang.reflect.Method constant = cls(type, "org.apache.sysds.runtime.compress.colgroup.ColGroupConst")
          .getMethod("create", indexClass, double.class);
      int rows = data.consumeInt(1, 16);
      double value = data.consumeInt(0, 3);
      java.util.List<Object> groups = new ArrayList<>();
      int n = data.consumeInt(1, 2);
      for (int i = 0; i < n; i++) {
        groups.add(constant.invoke(null, column, value));
      }
      return cls(type, "org.apache.sysds.runtime.compress.CompressedMatrixBlock")
          .getConstructor(int.class, int.class, long.class, boolean.class, java.util.List.class)
          .newInstance(rows, 1, value == 0 ? 0L : (long) rows, data.consumeBoolean(), groups);
    }
  }

  /**
   * An if-block as the DML compiler makes it: a predicate that writes a boolean to "__pred" (the
   * built block had none, so execute failed evaluating it), and 0..2 child blocks in each branch,
   * each with one scalar instruction; sometimes one reads an undefined variable, so the error
   * handling runs too.
   */
  static final class IfProgramBlock implements Recipe
  {
    private static final String INT = "·SCALAR·INT64·true";

    @Override
    public Object build(FuzzedDataProvider data, Class<?> type) throws Exception
    {
      Object program = cls(type, "org.apache.sysds.runtime.controlprogram.Program").getConstructor().newInstance();
      ArrayList<Object> predicate = new ArrayList<>();
      predicate.add(instruction(type, "CP°" + data.pickValue(new String[] {"==", "<", ">"}) + "°"
          + data.consumeInt(0, 3) + INT + "°" + data.consumeInt(0, 3) + INT + "°__pred·SCALAR·BOOLEAN"));
      Object block = type.getConstructor(program.getClass(), ArrayList.class).newInstance(program, predicate);
      for (String body : new String[] {"addProgramBlockIfBody", "addProgramBlockElseBody"}) {
        int n = data.consumeInt(0, 2);
        for (int i = 0; i < n; i++) {
          Object child = cls(type, "org.apache.sysds.runtime.controlprogram.BasicProgramBlock")
              .getConstructor(program.getClass()).newInstance(program);
          String first = data.consumeInt(0, 4) == 0 ? "missing·SCALAR·INT64" : data.consumeInt(0, 3) + INT;
          child.getClass().getMethod("addInstruction", cls(type, "org.apache.sysds.runtime.instructions.Instruction"))
              .invoke(child, instruction(type, "CP°+°" + first + "°" + data.consumeInt(0, 3) + INT + "°x·SCALAR·INT64"));
          type.getMethod(body, cls(type, "org.apache.sysds.runtime.controlprogram.ProgramBlock")).invoke(block, child);
        }
      }
      return block;
    }

    private static Object instruction(Class<?> type, String text) throws Exception
    {
      return cls(type, "org.apache.sysds.runtime.instructions.InstructionParser")
          .getMethod("parseSingleInstruction", String.class).invoke(null, text);
    }
  }

  /**
   * PageCache's static page pool is null until PageCache.init(), which systemds calls at start-up,
   * so every putPage / getPage failed on its first line. These are the arguments of the two static
   * methods: each starts the pool afresh (so the same input sees the same pool), then
   * "page" (for putPage): sometimes fills the pool past its cleanup threshold of 128 sizes, and
   * returns a small page; "size" (for getPage): puts pages of sizes 1..4 in the pool, and returns
   * one of those sizes or one with no pages. The pool is filled directly, not through putPage.
   */
  static final class PageCacheArgument implements Recipe
  {
    private final String what;

    PageCacheArgument(String what)
    {
      this.what = what;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Object build(FuzzedDataProvider data, Class<?> type) throws Exception
    {
      type.getMethod("init").invoke(null);
      java.lang.reflect.Field poolField = type.getDeclaredField("_pool");
      poolField.setAccessible(true);
      java.util.Map<Integer, java.util.LinkedList<java.lang.ref.SoftReference<byte[]>>> pool =
          (java.util.Map<Integer, java.util.LinkedList<java.lang.ref.SoftReference<byte[]>>>) poolField.get(null);
      if (what.equals("page")) {
        if (data.consumeBoolean()) {
          for (int size = 100; size < 230; size++) {
            pool.put(size, new java.util.LinkedList<java.lang.ref.SoftReference<byte[]>>());
          }
        }
        return data.consumeBytes(data.consumeInt(1, 4));
      }
      for (int size = 1; size <= 4; size++) {
        java.util.LinkedList<java.lang.ref.SoftReference<byte[]>> pages = new java.util.LinkedList<>();
        for (int k = data.consumeInt(0, 2); k > 0; k--) {
          pages.add(new java.lang.ref.SoftReference<>(new byte[size]));
        }
        pool.put(size, pages);
      }
      return data.consumeInt(1, 5);
    }
  }

  /**
   * A parfor result merge: an output matrix and 1..3 input matrices (column vectors of one length,
   * held in memory) as parfor hands them over, merged into a file in the side's own folder. Built
   * by autofuzz, the output had no metadata, so executeParallelMerge failed on its first line.
   */
  static final class ResultMerge implements Recipe
  {
    @Override
    public Object build(FuzzedDataProvider data, Class<?> type) throws Exception
    {
      int rows = data.consumeInt(1, 4);
      Object output = InstructionContext.matrixObject(type, "out", values(data, rows));
      Object[] inputs = (Object[]) java.lang.reflect.Array.newInstance(output.getClass(), data.consumeInt(1, 3));
      for (int i = 0; i < inputs.length; i++) {
        inputs[i] = InstructionContext.matrixObject(type, "in" + i, values(data, rows));
      }
      String file = new java.io.File(CommonRecipes.SideFolder.of(data, type), "merged").getPath();
      return type.getConstructor(output.getClass(), inputs.getClass(), String.class, boolean.class)
          .newInstance(output, inputs, file, data.consumeBoolean());
    }

    private static double[] values(FuzzedDataProvider data, int n)
    {
      double[] v = new double[n];
      for (int i = 0; i < n; i++) {
        v[i] = data.consumeInt(-3, 3);
      }
      return v;
    }
  }

  /**
   * The parfor heuristic optimizer with its cost estimator set, as optimize(...) sets it before
   * using it: built without, _cost was null and every isLargeProblem failed on its first line.
   * The memory cost model over an empty plan mapping: a generic leaf node costs 0, and a time
   * estimate for a HOP node is refused (DMLRuntimeException), the method's error branch.
   */
  static final class OptimizerHeuristic implements Recipe
  {
    @Override
    public Object build(FuzzedDataProvider data, Class<?> type) throws Exception
    {
      String opt = "org.apache.sysds.runtime.controlprogram.parfor.opt.";
      Object optimizer = type.getConstructor().newInstance();
      Class<?> mapping = cls(type, opt + "OptTreePlanMapping");
      Object estimator = cls(type, opt + "CostEstimatorHops").getConstructor(mapping)
          .newInstance(mapping.getConstructor().newInstance());
      java.lang.reflect.Field cost = cls(type, opt + "OptimizerRuleBased").getDeclaredField("_cost");
      cost.setAccessible(true);
      cost.set(optimizer, estimator);
      return optimizer;
    }
  }

  /**
   * A frame column of booleans (ABooleanArray): a BooleanArray or a BitSetArray over 1..8 fuzzed
   * values. The engine tried only an empty array, which both reject ("Invalid zero/negative size").
   */
  static final class BooleanColumn implements Recipe
  {
    @Override
    public Object build(FuzzedDataProvider data, Class<?> type) throws Exception
    {
      boolean[] values = new boolean[data.consumeInt(1, 8)];
      for (int i = 0; i < values.length; i++) {
        values[i] = data.consumeBoolean();
      }
      String kind = data.consumeBoolean() ? "BooleanArray" : "BitSetArray";
      return cls(type, "org.apache.sysds.runtime.frame.data.columns." + kind)
          .getConstructor(boolean[].class).newInstance((Object) values);
    }
  }

  /**
   * An iterator over 0..3 small matrices (AComEst.matrix), as Spark hands a partition's blocks to
   * a function: nothing could build an Iterator.
   */
  static final class MatrixBlockIterator implements Recipe
  {
    @Override
    public Object build(FuzzedDataProvider data, Class<?> type) throws Exception
    {
      ArrayList<Object> blocks = new ArrayList<>();
      for (int n = data.consumeInt(0, 3); n > 0; n--) {
        blocks.add(AComEst.matrix(data, type));
      }
      return blocks.iterator();
    }
  }

  /**
   * The 20 bytes of an HDF5 datatype message for a floating-point type, as H5DoubleDataType reads
   * them: class 1 / version 1, the class bit field (byte order and mantissa normalization fuzzed;
   * in about one of four the VAX-order bit, which the parser rejects), size 8, then an IEEE
   * double's layout. A random buffer was almost always too short.
   */
  static final class H5DoubleDataTypeBytes implements Recipe
  {
    @Override
    public Object build(FuzzedDataProvider data, Class<?> type)
    {
      java.nio.ByteBuffer bb = java.nio.ByteBuffer.allocate(20).order(java.nio.ByteOrder.LITTLE_ENDIAN);
      bb.put((byte) 0x11);                                  // class 1 (floating point), version 1
      int bits = (data.consumeBoolean() ? 0x01 : 0)         // big endian
          | (data.consumeInt(0, 2) << 4)                    // mantissa normalization
          | (data.consumeInt(0, 3) == 0 ? 0x40 : 0);        // VAX order (rejected)
      bb.put((byte) bits).put((byte) 63).put((byte) 0);    // sign bit location 63
      bb.putInt(8);                                         // size in bytes
      bb.putShort((short) 0).putShort((short) 64);          // bit offset, precision
      bb.put((byte) 52).put((byte) 11).put((byte) 0).put((byte) 52); // exponent and mantissa
      bb.putInt(1023);                                      // exponent bias
      bb.flip();
      return bb;
    }
  }

  /**
   * A DependencyTask around a callable that records its calls (and returns a small number), with
   * 0..2 dependent tasks registered as waiting on it. Built by autofuzz, the task was null and
   * call() failed on its first line. With no thread pool set, a dependent that becomes ready makes
   * call() throw, as it does in systemds; that branch is reachable too.
   */
  static final class DependencyTask implements Recipe
  {
    @Override
    public Object build(FuzzedDataProvider data, Class<?> type) throws Exception
    {
      java.lang.reflect.Constructor<?> make = type.getConstructor(java.util.concurrent.Callable.class, java.util.List.class);
      CommonRecipes.Recorder callable = new CommonRecipes.Recorder("java.util.concurrent.Callable");
      Object task = make.newInstance(callable.build(data, type), new ArrayList<Object>());
      for (int n = data.consumeInt(0, 2); n > 0; n--) {
        type.getMethod("addDependent", type)
            .invoke(task, make.newInstance(callable.build(data, type), new ArrayList<Object>()));
      }
      return task;
    }
  }

  /**
   * For the abstract DependencyWrapperTask: its one subclass, MultiColumnEncoder's private
   * ApplyTasksWrapperTask (no encoder or pool: the methods tested here do not use them), with its
   * submission marked done, the state after its call(): getWrappedTaskFuture() waits for that.
   */
  static final class DependencyWrapperTask implements Recipe
  {
    @Override
    public Object build(FuzzedDataProvider data, Class<?> type) throws Exception
    {
      Class<?> wrapper = cls(type, "org.apache.sysds.runtime.transform.encode.MultiColumnEncoder$ApplyTasksWrapperTask");
      java.lang.reflect.Constructor<?> make = wrapper.getDeclaredConstructors()[0];
      make.setAccessible(true);
      Object task = make.newInstance(new Object[make.getParameterCount()]);
      // by type, not name: the refactoring renamed the field (_submitted -> submitted)
      for (java.lang.reflect.Field f : type.getDeclaredFields()) {
        if (f.getType() == java.util.concurrent.CompletableFuture.class) {
          f.setAccessible(true);
          ((java.util.concurrent.CompletableFuture<?>) f.get(task)).complete(null);
        }
      }
      return task;
    }
  }

  /** A finished Future holding a finished Future of a small number. */
  static final class CompletedFuture implements Recipe
  {
    @Override
    public Object build(FuzzedDataProvider data, Class<?> type)
    {
      return java.util.concurrent.CompletableFuture.completedFuture(
          java.util.concurrent.CompletableFuture.completedFuture(data.consumeInt(0, 9)));
    }
  }

  /**
   * A FastStringTokenizer given its text, as systemds does with reset(text) before reading tokens:
   * built without, the text was null and nextToken failed on its first line. A delimiter from
   * , ; space or tab, and a text of 0..5 short words joined by 1..2 delimiters, sometimes with
   * leading ones, so empty, all-delimiter and normal inputs all occur.
   */
  static final class FastStringTokenizer implements Recipe
  {
    @Override
    public Object build(FuzzedDataProvider data, Class<?> type) throws Exception
    {
      char delimiter = data.pickValue(new Character[] {',', ';', ' ', '\t'});
      StringBuilder text = new StringBuilder();
      for (int n = data.consumeInt(0, 5); n > 0; n--) {
        for (int d = data.consumeInt(0, 2); d > 0; d--) {
          text.append(delimiter);
        }
        text.append(data.pickValue(new String[] {"a", "bc", "1.5", "x y"}));
      }
      Object tokenizer = type.getConstructor(char.class).newInstance(delimiter);
      type.getMethod("reset", String.class).invoke(tokenizer, text.toString());
      return tokenizer;
    }
  }

  /**
   * ColGroupDDC: autofuzz built it with an empty dictionary, so every dictionary operation failed on
   * its first line. Through systemds' own factory: columns 0..k-1 (k 1..4; rexpandCols takes only one),
   * 2..3 distinct tuples of values 0..3 (the first nonzero, so the dictionary is not empty), and rows
   * that use every tuple.
   */
  static final class ColGroupDDC implements Recipe
  {
    @Override
    public Object build(FuzzedDataProvider data, Class<?> type) throws Exception
    {
      int cols = data.consumeInt(1, 4);
      int unique = data.consumeInt(2, 3);
      double[] values = new double[cols * unique];
      for (int i = 0; i < values.length; i++) {
        values[i] = i == 0 ? data.consumeInt(1, 3) : data.consumeInt(0, 3);
      }
      int[] rows = new int[unique + data.consumeInt(0, 4)];
      for (int r = 0; r < rows.length; r++) {
        rows[r] = r < unique ? r : data.consumeInt(0, unique - 1);
      }
      String pkg = "org.apache.sysds.runtime.compress.colgroup.";
      Object index = cls(type, pkg + "indexes.ColIndexFactory").getMethod("create", int.class).invoke(null, cols);
      Object dict = cls(type, pkg + "dictionary.Dictionary").getMethod("create", double[].class)
          .invoke(null, (Object) values);
      Object map = cls(type, pkg + "mapping.MapToFactory").getMethod("create", int[].class, int.class)
          .invoke(null, rows, unique);
      return type.getMethod("create", cls(type, pkg + "indexes.IColIndex"), cls(type, pkg + "dictionary.IDictionary"),
          cls(type, pkg + "mapping.AMapToData"), int[].class).invoke(null, index, dict, map, null);
    }
  }

  /**
   * ReaderColumnSelectionSparseDelta: autofuzz built it over a dense matrix, so its sparse block was
   * null and processInRange failed on its first line. systemds' own factory, over a sparse 4..8 x
   * 1..6 matrix of values 0..3 with no empty row (getNextRow only processes non-empty rows), reading
   * a nonempty subset of its columns.
   */
  static final class SparseDeltaReader implements Recipe
  {
    @Override
    public Object build(FuzzedDataProvider data, Class<?> type) throws Exception
    {
      Class<?> mb = cls(type, "org.apache.sysds.runtime.matrix.data.MatrixBlock");
      int rows = data.consumeInt(4, 8);
      int cols = data.consumeInt(1, 6);
      Object m = mb.getConstructor(int.class, int.class, boolean.class).newInstance(rows, cols, false);
      java.lang.reflect.Method set = mb.getMethod("set", int.class, int.class, double.class);
      for (int i = 0; i < rows; i++) {
        boolean empty = true;
        for (int j = 0; j < cols; j++) {
          int v = data.consumeInt(0, 3);
          set.invoke(m, i, j, (double) v);
          empty &= v == 0;
        }
        if (empty) {
          set.invoke(m, i, data.consumeInt(0, cols - 1), (double) data.consumeInt(1, 3));
        }
      }
      mb.getMethod("denseToSparse", boolean.class, int.class).invoke(m, data.consumeBoolean(), 1);
      java.util.List<Integer> chosen = new ArrayList<>();
      for (int j = 0; j < cols; j++) {
        if (data.consumeBoolean()) {
          chosen.add(j);
        }
      }
      if (chosen.isEmpty()) {
        chosen.add(data.consumeInt(0, cols - 1));
      }
      int[] picked = new int[chosen.size()];
      for (int i = 0; i < picked.length; i++) {
        picked[i] = chosen.get(i);
      }
      Class<?> indexClass = cls(type, "org.apache.sysds.runtime.compress.colgroup.indexes.IColIndex");
      Object index = cls(type, "org.apache.sysds.runtime.compress.colgroup.indexes.ColIndexFactory")
          .getMethod("create", int[].class).invoke(null, (Object) picked);
      return cls(type, "org.apache.sysds.runtime.compress.readers.ReaderColumnSelection")
          .getMethod("createDeltaReader", mb, indexClass, boolean.class, int.class, int.class)
          .invoke(null, m, index, false, 0, rows);
    }
  }

  /**
   * LocalParamServer: built bare, its model map was null and pull failed on its first line. As the
   * server sets it up: 1..3 workers, each with a queue of one, here already holding the model the
   * server broadcast to it (a list of 0..3 integers). Every queue is filled, since pull waits on an
   * empty one forever.
   */
  static final class ParamServer implements Recipe
  {
    @Override
    public Object build(FuzzedDataProvider data, Class<?> type) throws Exception
    {
      Class<?> dataClass = cls(type, "org.apache.sysds.runtime.instructions.cp.Data");
      java.lang.reflect.Constructor<?> intObject =
          cls(type, "org.apache.sysds.runtime.instructions.cp.IntObject").getConstructor(long.class);
      java.lang.reflect.Constructor<?> listObject =
          cls(type, "org.apache.sysds.runtime.instructions.cp.ListObject").getConstructor(java.util.List.class);
      java.util.Map<Integer, java.util.concurrent.BlockingQueue<Object>> models = new java.util.HashMap<>();
      for (int w = data.consumeInt(1, 3) - 1; w >= 0; w--) {
        java.util.List<Object> values = new ArrayList<>();
        for (int n = data.consumeInt(0, 3); n > 0; n--) {
          values.add(dataClass.cast(intObject.newInstance((long) data.consumeInt(0, 9))));
        }
        java.util.concurrent.BlockingQueue<Object> queue = new java.util.concurrent.ArrayBlockingQueue<>(1);
        queue.add(listObject.newInstance(values));
        models.put(w, queue);
      }
      Object server = type.getConstructor().newInstance();
      java.lang.reflect.Field field =
          cls(type, "org.apache.sysds.runtime.controlprogram.paramserv.ParamServer").getDeclaredField("_modelMap");
      field.setAccessible(true);
      field.set(server, models);
      return server;
    }
  }

  /**
   * PackedBlock: its public constructor makes the empty block that read(...) fills in, so values was
   * null and tryWrite failed on its first line. As PackBuilder seals one: 0..3 values with their sizes,
   * each a small indexed matrix block, an empty nested packed block, or a value the spill registry
   * cannot write (a string or null), so every branch of tryWrite is reachable.
   */
  static final class PackedBlock implements Recipe
  {
    @Override
    public Object build(FuzzedDataProvider data, Class<?> type) throws Exception
    {
      java.lang.reflect.Constructor<?> packed = type.getDeclaredConstructor(Object[].class, long[].class, long.class);
      packed.setAccessible(true);
      Class<?> mb = cls(type, "org.apache.sysds.runtime.matrix.data.MatrixBlock");
      Class<?> mi = cls(type, "org.apache.sysds.runtime.matrix.data.MatrixIndexes");
      java.lang.reflect.Constructor<?> indexed = cls(type, "org.apache.sysds.runtime.instructions.spark.data.IndexedMatrixValue")
          .getConstructor(mi, cls(type, "org.apache.sysds.runtime.matrix.data.MatrixValue"));
      Object[] values = new Object[data.consumeInt(0, 3)];
      long[] sizes = new long[values.length];
      long total = 0;
      for (int i = 0; i < values.length; i++) {
        switch (data.consumeInt(0, 3)) {
          case 0:
            int rows = data.consumeInt(1, 3);
            int cols = data.consumeInt(1, 3);
            Object block = mb.getConstructor(int.class, int.class, double.class)
                .newInstance(rows, cols, (double) data.consumeInt(0, 3));
            Object ix = mi.getConstructor(long.class, long.class).newInstance(data.consumeLong(1, 4), data.consumeLong(1, 4));
            values[i] = indexed.newInstance(ix, block);
            break;
          case 1:
            values[i] = packed.newInstance(new Object[0], new long[0], 0L);
            break;
          case 2:
            values[i] = "not spillable";
            break;
          default:
            values[i] = null;
        }
        sizes[i] = data.consumeLong(0, 1000);
        total += sizes[i];
      }
      return packed.newInstance(values, sizes, total);
    }
  }

  /** The output columns of a slice: 0..n-1 for n 1..4, as AColGroup.sliceMultiColumns(cl, cu) makes them. */
  static final class ColumnRange implements Recipe
  {
    @Override
    public Object build(FuzzedDataProvider data, Class<?> type) throws Exception
    {
      return cls(type, "org.apache.sysds.runtime.compress.colgroup.indexes.ColIndexFactory")
          .getMethod("create", int.class).invoke(null, data.consumeInt(1, 4));
    }
  }

  /**
   * AComEst: abstract; autofuzz scanned the class path for it. systemds' own factory, over a small
   * matrix and the fixed-seed settings above.
   */
  static final class AComEst implements Recipe
  {
    @Override
    public Object build(FuzzedDataProvider data, Class<?> type) throws Exception
    {
      Object m = matrix(data, type);
      Object cs = new CompressionSettings().build(data, type);
      return cls(type, "org.apache.sysds.runtime.compress.estim.ComEstFactory")
          .getMethod("createEstimator", m.getClass(), cs.getClass(), int.class).invoke(null, m, cs, 1);
    }

    /** A dense matrix of up to 16 x 8 cells with few distinct values, as compression expects. */
    static Object matrix(FuzzedDataProvider data, Class<?> type) throws Exception
    {
      Class<?> mb = cls(type, "org.apache.sysds.runtime.matrix.data.MatrixBlock");
      int rows = data.consumeInt(1, 16);
      int cols = data.consumeInt(1, 8);
      Object m = mb.getConstructor(int.class, int.class, boolean.class).newInstance(rows, cols, false);
      java.lang.reflect.Method set = mb.getMethod("set", int.class, int.class, double.class);
      for (int i = 0; i < rows; i++) {
        for (int j = 0; j < cols; j++) {
          set.invoke(m, i, j, (double) data.consumeInt(0, 3));
        }
      }
      return m;
    }
  }

  /**
   * CompressedWriteBlock: autofuzz built it with no matrix inside, so CompressUnwrap.call failed on
   * it every time. A block holding a small matrix.
   */
  static final class CompressedWriteBlock implements Recipe
  {
    @Override
    public Object build(FuzzedDataProvider data, Class<?> type) throws Exception
    {
      Object m = AComEst.matrix(data, type);
      return type.getConstructor(m.getClass()).newInstance(m);
    }
  }

  /**
   * ACachingMBDictionary: autofuzz built a Dictionary with no values, and every getMBDict divided
   * by zero. A dictionary of one to sixteen values.
   */
  static final class Dictionary implements Recipe
  {
    @Override
    public Object build(FuzzedDataProvider data, Class<?> type) throws Exception
    {
      double[] values = new double[data.consumeInt(1, 16)];
      for (int i = 0; i < values.length; i++) {
        values[i] = data.consumeInt(-3, 3);
      }
      return cls(type, "org.apache.sysds.runtime.compress.colgroup.dictionary.Dictionary")
          .getMethod("createNoCheck", double[].class).invoke(null, (Object) values);
    }
  }

  /** A DataIdentifier as the parser leaves it after validation: typed, sized, its own output. */
  private static Object identifier(FuzzedDataProvider data, Class<?> type) throws Exception
  {
    boolean matrix = data.consumeBoolean();
    Object dt = enumValue(type, "org.apache.sysds.common.Types$DataType", matrix ? "MATRIX" : "SCALAR");
    Object vt = enumValue(type, "org.apache.sysds.common.Types$ValueType", "FP64");
    Class<?> c = cls(type, "org.apache.sysds.parser.DataIdentifier");
    Object id = c.getConstructor(String.class, dt.getClass(), vt.getClass())
        .newInstance(data.pickValue(new String[] {"A", "B", "X"}), dt, vt);
    if (matrix) {
      c.getMethod("setDimensions", long.class, long.class)
          .invoke(id, data.consumeLong(1, 16), data.consumeLong(1, 16));
    }
    setOutput(type, id, id);
    return id;
  }

  private static void setOutput(Class<?> type, Object expr, Object output) throws Exception
  {
    cls(type, "org.apache.sysds.parser.Expression")
        .getMethod("setOutput", cls(type, "org.apache.sysds.parser.Identifier")).invoke(expr, output);
  }

  /**
   * BinaryExpression: autofuzz built it with no operands, so checkAndSetDimensions failed on its
   * first line. A fuzzed operator over two typed identifiers.
   */
  static final class BinaryExpression implements Recipe
  {
    @Override
    public Object build(FuzzedDataProvider data, Class<?> type) throws Exception
    {
      Object op = enumValue(type, "org.apache.sysds.parser.Expression$BinaryOp",
          data.pickValue(new String[] {"PLUS", "MINUS", "MULT", "DIV", "MODULUS", "INTDIV", "MATMULT", "POW"}));
      Class<?> c = cls(type, "org.apache.sysds.parser.BinaryExpression");
      Class<?> expr = cls(type, "org.apache.sysds.parser.Expression");
      Object e = c.getConstructor(op.getClass()).newInstance(op);
      c.getMethod("setLeft", expr).invoke(e, identifier(data, type));
      c.getMethod("setRight", expr).invoke(e, identifier(data, type));
      return e;
    }
  }

  /**
   * BooleanExpression: autofuzz built it with no operands, so validateExpression failed on its
   * first line. A fuzzed operator over boolean literals (NOT has no right operand).
   */
  static final class BooleanExpression implements Recipe
  {
    @Override
    public Object build(FuzzedDataProvider data, Class<?> type) throws Exception
    {
      String name = data.pickValue(new String[] {"CONDITIONALAND", "CONDITIONALOR", "LOGICALAND", "LOGICALOR", "NOT"});
      Object op = enumValue(type, "org.apache.sysds.parser.Expression$BooleanOp", name);
      Class<?> c = cls(type, "org.apache.sysds.parser.BooleanExpression");
      Class<?> expr = cls(type, "org.apache.sysds.parser.Expression");
      Object e = c.getConstructor(op.getClass()).newInstance(op);
      c.getMethod("setLeft", expr).invoke(e, literal(data, type));
      if (!name.equals("NOT")) {
        c.getMethod("setRight", expr).invoke(e, literal(data, type));
      }
      return e;
    }

    private static Object literal(FuzzedDataProvider data, Class<?> type) throws Exception
    {
      Object b = cls(type, "org.apache.sysds.parser.BooleanIdentifier")
          .getConstructor(boolean.class).newInstance(data.consumeBoolean());
      setOutput(type, b, b);
      return b;
    }
  }

  /** ACostEstimate: abstract; autofuzz scanned the class path for it. systemds' own factory. */
  static final class ACostEstimate implements Recipe
  {
    @Override
    public Object build(FuzzedDataProvider data, Class<?> type) throws Exception
    {
      Object cs = new CompressionSettings().build(data, type);
      Class<?> builder = cls(type, "org.apache.sysds.runtime.compress.cost.CostEstimatorBuilder");
      return cls(type, "org.apache.sysds.runtime.compress.cost.CostEstimatorFactory")
          .getMethod("create", cs.getClass(), builder, int.class, int.class, double.class)
          .invoke(null, cs, null, data.consumeInt(1, 1000), data.consumeInt(1, 100), data.consumeRegularDouble(0, 1));
    }
  }

  /**
   * H5ByteReader: its implementations are private classes of ReaderHDF5 with long names, which the
   * subtype search reaches only after loading most of org.apache. The in-memory one, over fuzzed
   * bytes.
   */
  static final class H5ByteReader implements Recipe
  {
    @Override
    public Object build(FuzzedDataProvider data, Class<?> type) throws Exception
    {
      java.lang.reflect.Constructor<?> ctor =
          cls(type, "org.apache.sysds.runtime.io.ReaderHDF5$ByteArrayH5ByteReader")
              .getDeclaredConstructor(byte[].class);
      ctor.setAccessible(true);
      return ctor.newInstance((Object) data.consumeBytes(512));
    }
  }

  /**
   * Sql: its constructor reads the conn/user/password/query lops out of the parameter map and fails
   * on an empty one. Each is a string literal lop, as a parsed sql() call would give it.
   */
  static final class Sql implements Recipe
  {
    @Override
    public Object build(FuzzedDataProvider data, Class<?> type) throws Exception
    {
      Object vt = enumValue(type, "org.apache.sysds.common.Types$ValueType", "STRING");
      java.lang.reflect.Method literal = cls(type, "org.apache.sysds.lops.Data")
          .getMethod("createLiteralLop", vt.getClass(), String.class);
      java.util.HashMap<String, Object> params = new java.util.HashMap<>();
      for (String key : new String[] {"conn", "user", "password", "query"}) {
        params.put(key, literal.invoke(null, vt, data.consumeString(12)));
      }
      Object dt = enumValue(type, "org.apache.sysds.common.Types$DataType", "MATRIX");
      Object resultVt = enumValue(type, "org.apache.sysds.common.Types$ValueType", "FP64");
      return type.getConstructor(java.util.HashMap.class, dt.getClass(), resultVt.getClass())
          .newInstance(params, dt, resultVt);
    }
  }
}
