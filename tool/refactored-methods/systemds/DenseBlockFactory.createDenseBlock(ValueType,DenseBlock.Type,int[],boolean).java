public static DenseBlock createDenseBlock(ValueType vt, DenseBlock.Type type, int[] dims, boolean dedup) {
    if (dedup) {
        return createDenseBlockWithDedup(vt, type, dims);
    }
    return createDenseBlockWithoutDedup(vt, type, dims);
}
// ---- helper method(s) introduced by the refactoring ----
private static DenseBlock createDenseBlockWithDedup(ValueType vt, DenseBlock.Type type, int[] dims) {
    switch(type) {
        case DRB:
            switch(vt) {
                case FP64:
                    return new DenseBlockFP64DEDUP(dims);
                default:
                    throw new DMLRuntimeException("Unsupported dense block value type with deduplication enabled: " + vt.name());
            }
        case LDRB:
            switch(vt) {
                default:
                    throw new NotImplementedException();
            }
        default:
            throw new DMLRuntimeException("Unexpected dense block type: " + type.name());
    }
}

private static DenseBlock createDenseBlockWithoutDedup(ValueType vt, DenseBlock.Type type, int[] dims) {
    switch(type) {
        case DRB:
            return createDenseBlockDRB(vt, dims);
        case LDRB:
            return createDenseBlockLDRB(vt, dims);
        default:
            throw new DMLRuntimeException("Unexpected dense block type: " + type.name());
    }
}

private static DenseBlock createDenseBlockDRB(ValueType vt, int[] dims) {
    switch(vt) {
        case FP32:
            return new DenseBlockFP32(dims);
        case FP64:
            return new DenseBlockFP64(dims);
        case INT32:
            return new DenseBlockInt32(dims);
        case INT64:
            return new DenseBlockInt64(dims);
        case BOOLEAN:
            return new DenseBlockBool(dims);
        case STRING:
            return new DenseBlockString(dims);
        default:
            throw new DMLRuntimeException("Unsupported dense block value type: " + vt.name());
    }
}

private static DenseBlock createDenseBlockLDRB(ValueType vt, int[] dims) {
    switch(vt) {
        case FP32:
            return new DenseBlockLFP32(dims);
        case FP64:
            return new DenseBlockLFP64(dims);
        case BOOLEAN:
            return new DenseBlockLBool(dims);
        case INT32:
            return new DenseBlockLInt32(dims);
        case INT64:
            return new DenseBlockLInt64(dims);
        case STRING:
            return new DenseBlockLString(dims);
        default:
            throw new NotImplementedException();
    }
}

