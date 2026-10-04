public static LineageItem parseLineageTrace(String str, String name) {
    ExecutionContext ec = ExecutionContextFactory.createContext();
    LineageItem li = null;
    Map<Long, LineageItem> map = new HashMap<>();
    for (String line : str.split("\\r?\\n")) {
        li = null;
        Map<String, String> tokens = lineageTraceTokenizer.tokenize(line);
        Long id = Long.valueOf(tokens.get("id"));
        LineageItem.LineageItemType type = LineageItemUtils.getType(tokens.get("type"));
        String representation = tokens.get("representation");
        switch(type) {
            case Creation:
                li = handleCreation(id, representation);
                break;
            case Literal:
                li = new LineageItem(id, representation);
                break;
            case Instruction:
            case Dedup:
                li = parseLineageInstruction(id, representation, map, name);
                break;
            default:
                throw new ParseException("Invalid LineageItemType given");
        }
        map.put(id, li);
    }
    return li;
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

