public static String displayReusableInsts() {
    // Total time saved and reuse counts per opcode, ordered by saved time
    StringBuilder sb = new StringBuilder();
    sb.append("# Instruction\tTime(s)  Count \n");
    int instCount = Math.min(INSTCOUNT, LineageEstimator.computeSavingInst.size());
    for (int i = 1; i <= instCount; i++) {
        MutableTriple<String, Long, Double> op = LineageEstimator.computeSavingInst.poll();
        int tl = String.valueOf(op.getRight() * 1e-3).indexOf(".");
        if (op.getRight() > 0)
            sb.append(formatInstructionLine(i, op.getLeft(), op.getRight(), op.getMiddle(), tl));
    }
    return sb.toString();
}
// ---- helper method(s) introduced by the refactoring ----
private static String formatTimeInSeconds(double nanoseconds) {
    return String.format("%.3f", nanoseconds * 1e-9);
}

private static String formatSizeInMB(double bytes) {
    return String.format("%.3f", bytes / (1024 * 1024));
}

private static String formatInstructionLine(int index, String opcode, double time, long count, int decimalIndex) {
    return String.valueOf(index) + // 4-length(i) spaces
    String.format("%" + (4 - String.valueOf(index).length()) + "s", "") + opcode + // 15 - length(opcode) spaces
    String.format("%" + (15 - opcode.length()) + "s", "") + String.format("%.3f", time * 1e-3) + // 8 - length(time upto '.') spaces
    String.format("%" + (8 - (decimalIndex + 3)) + "s", "") + count + "\n";
}

