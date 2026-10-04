public static String displayReusableInsts() {
    // Total time saved and reuse counts per opcode, ordered by saved time
    StringBuilder sb = new StringBuilder();
    sb.append("# Instrunction\t" + "  " + "Time(s)  Count \n");
    int instCount = Math.min(INSTCOUNT, LineageEstimator.computeSavingInst.size());
    for (int i = 1; i <= instCount; i++) {
        MutableTriple<String, Long, Double> op = LineageEstimator.computeSavingInst.poll();
        int tl = String.valueOf(op.getRight() * 1e-3).indexOf(".");
        if (op.getRight() > 0)
            sb.append(String.valueOf(i) + // 4-length(i) spaces
            String.format("%" + (4 - String.valueOf(i).length()) + "s", "") + op.getLeft() + // 15 - length(opcode) spaces
            String.format("%" + (15 - op.getLeft().length()) + "s", "") + String.format("%.3f", op.getRight() * 1e-3) + // 8 - length(time upto '.') spaces
            String.format("%" + (8 - (tl + 3)) + "s", "") + op.getMiddle() + "\n");
    }
    return sb.toString();
}