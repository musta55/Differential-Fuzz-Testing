public static void updateAppendDataCharacteristics(DataCharacteristics mc1, DataCharacteristics mc2, DataCharacteristics mcOut, boolean cbind) {
    if (!mcOut.dimsKnown()) {
        if (!mc1.dimsKnown() || !mc2.dimsKnown())
            throw new DMLRuntimeException("The output dimensions are not specified and cannot be inferred from inputs.");
        if (cbind)
            mcOut.set(mc1.getRows(), mc1.getCols() + mc2.getCols(), mc1.getBlocksize());
        else
            //rbind
            mcOut.set(mc1.getRows() + mc2.getRows(), mc1.getCols(), mc1.getBlocksize());
    }
}