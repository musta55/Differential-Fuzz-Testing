private static LineageItem parseLineageInstruction(Long id, String str, Map<Long, LineageItem> map, String name) {
    String[] tokens = str.split(" ");
    if (tokens.length < 2)
        throw new ParseException("Invalid length ot lineage item " + tokens.length + ".");
    String opcode = tokens[0];
    /*if (opcode.startsWith(LineageItemUtils.LPLACEHOLDER)) {
			// Convert this to a leaf node (creation type)
			String data = opcode;
			return new LineageItem(id, data, "Create"+opcode);
		}*/
    ArrayList<LineageItem> inputs = new ArrayList<>();
    int specialValueBits = 0;
    for (int i = 1; i < tokens.length; i++) {
        String token = tokens[i];
        if (token.startsWith("(") && token.endsWith(")")) {
            //rm parentheses
            token = token.substring(1, token.length() - 1);
            inputs.add(map.get(Long.valueOf(token)));
        } else if (token.startsWith("[") && token.endsWith("]")) {
            //rm parentheses
            token = token.substring(1, token.length() - 1);
            specialValueBits = Integer.parseInt(token);
        } else
            throw new ParseException("Invalid format for LineageItem reference");
    }
    return new LineageItem(id, "", opcode, inputs.toArray(new LineageItem[0]), specialValueBits);
}