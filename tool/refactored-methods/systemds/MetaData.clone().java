@Override
public Object clone() {
    DataCharacteristics clonedDc = createDataCharacteristics(_dc);
    return new MetaData(clonedDc);
}
// ---- helper method(s) introduced by the refactoring ----
private DataCharacteristics createDataCharacteristics(DataCharacteristics dc) {
    if (dc instanceof MatrixCharacteristics)
        return new MatrixCharacteristics(dc);
    else if (dc instanceof TensorCharacteristics)
        return new TensorCharacteristics(dc);
    else
        throw new IllegalArgumentException("Unsupported DataCharacteristics type");
}

