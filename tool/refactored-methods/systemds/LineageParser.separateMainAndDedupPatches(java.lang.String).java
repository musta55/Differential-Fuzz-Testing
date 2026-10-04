protected static String[] separateMainAndDedupPatches(String str) {
    str = normalizeNewLines(str);
    String[] allPatches = str.split("\n\n");
    if (allPatches.length == 1) {
        return allPatches;
    }
    String[] patches = new String[2];
    patches[0] = allPatches[0];
    StringBuilder sb = new StringBuilder();
    for (int i = 1; i < allPatches.length; i++) {
        sb.append(allPatches[i]).append("\n\n");
    }
    patches[1] = sb.toString();
    return patches;
}
// ---- helper method(s) introduced by the refactoring ----
private static LineageItem handleCreation(Long id, String representation) {
    if (representation.startsWith(LineageItemUtils.LPLACEHOLDER)) {
        return new LineageItem(id, representation, "Create" + representation);
    }
    Instruction inst = InstructionParser.parseSingleInstruction(representation);
    if (!(inst instanceof LineageTraceable))
        throw new ParseException("Invalid Instruction (" + inst.getOpcode() + ") traced");
    Pair<String, LineageItem> item = ((LineageTraceable) inst).getLineageItem(ExecutionContextFactory.createContext());
    if (item == null)
        throw new ParseException("Instruction without output (" + inst.getOpcode() + ") not supported");
    return new LineageItem(id, item.getValue());
}

private static String normalizeNewLines(String str) {
    return str.replace("\r\n", "\n");
}

