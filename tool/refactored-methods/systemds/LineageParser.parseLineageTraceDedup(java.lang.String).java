protected static void parseLineageTraceDedup(String str) {
    str = normalizeNewLines(str);
    String[] allPatches = str.split("\n\n");
    for (String patch : allPatches) {
        String[] headBody = patch.split("\n", 2);
        String[] parts = headBody[0].split(LineageDedupUtils.DEDUP_DELIM);
        LineageItem patchLi = parseLineageTrace(headBody[1]);
        Long pathId = Long.parseLong(parts[3]);
        String loopName = parts[2];
        DedupLoopItem loopItem = LineageRecomputeUtils.loopPatchMap.computeIfAbsent(loopName, k -> new DedupLoopItem(loopName));
        if (!loopItem.patchLiMap.containsKey(pathId)) {
            loopItem.patchLiMap.put(pathId, new HashMap<>());
        }
        loopItem.patchLiMap.get(pathId).put(parts[1], patchLi);
    }
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

