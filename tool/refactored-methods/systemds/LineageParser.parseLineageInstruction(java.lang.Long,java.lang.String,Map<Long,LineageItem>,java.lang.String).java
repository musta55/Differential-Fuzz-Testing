private static LineageItem parseLineageInstruction(Long id, String str, Map<Long, LineageItem> map, String name) {
    String[] tokens = str.split(" ");
    if (tokens.length < 2)
        throw new ParseException("Invalid length ot lineage item " + tokens.length + ".");
    String opcode = tokens[0];
    ArrayList<LineageItem> inputs = new ArrayList<>();
    int specialValueBits = 0;
    for (int i = 1; i < tokens.length; i++) {
        String token = tokens[i];
        if (token.startsWith("(") && token.endsWith(")")) {
            inputs.add(map.get(Long.valueOf(token.substring(1, token.length() - 1))));
        } else if (token.startsWith("[") && token.endsWith("]")) {
            specialValueBits = Integer.parseInt(token.substring(1, token.length() - 1));
        } else
            throw new ParseException("Invalid format for LineageItem reference");
    }
    return new LineageItem(id, "", opcode, inputs.toArray(new LineageItem[0]), specialValueBits);
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

