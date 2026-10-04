/**
 * Constructor to setup a partial Matrix-Vector Multiplication for Spark
 *
 * @param input1 low-level operator 1
 * @param input2 low-level operator 2
 * @param dt data type
 * @param vt value type
 * @param rightCache true if right cache, false if left cache
 * @param partitioned true if partitioned, false if not partitioned
 * @param emptyBlocks true if output empty blocks
 * @param aggtype spark aggregation type
 */
public MapMult(Lop input1, Lop input2, DataType dt, ValueType vt, boolean rightCache, boolean partitioned, boolean emptyBlocks, SparkAggType aggtype) {
    super(Lop.Type.MapMult, dt, vt);
    addInput(input1);
    addInput(input2);
    input1.addOutput(this);
    input2.addOutput(this);
    //setup mapmult parameters
    if (rightCache)
        _cacheType = partitioned ? CacheType.RIGHT_PART : CacheType.RIGHT;
    else
        _cacheType = partitioned ? CacheType.LEFT_PART : CacheType.LEFT;
    _outputEmptyBlocks = emptyBlocks;
    _aggtype = aggtype;
    lps.setProperties(inputs, ExecType.SPARK);
}