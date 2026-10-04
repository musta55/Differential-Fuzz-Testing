public static void updateAppendDataCharacteristics(DataCharacteristics mc1, DataCharacteristics mc2, DataCharacteristics mcOut, boolean cbind) {
    if (!mcOut.dimsKnown()) {
        validateInputDimensions(mc1, mc2);
        if (cbind)
            setCBindDimensions(mc1, mc2, mcOut);
        else
            //rbind
            setRBindDimensions(mc1, mc2, mcOut);
    }
}
// ---- helper method(s) introduced by the refactoring ----
private static void validateInputDimensions(DataCharacteristics mc1, DataCharacteristics mc2) {
    if (!mc1.dimsKnown() || !mc2.dimsKnown())
        throw new DMLRuntimeException("The output dimensions are not specified and cannot be inferred from inputs.");
}

private static void setCBindDimensions(DataCharacteristics mc1, DataCharacteristics mc2, DataCharacteristics mcOut) {
    mcOut.set(mc1.getRows(), mc1.getCols() + mc2.getCols(), mc1.getBlocksize());
}

private static void setRBindDimensions(DataCharacteristics mc1, DataCharacteristics mc2, DataCharacteristics mcOut) {
    mcOut.set(mc1.getRows() + mc2.getRows(), mc1.getCols(), mc1.getBlocksize());
}

