@Override
public String toString() {
    if (subTokens.isEmpty()) {
        return EMPTY_TOKEN;
    }
    if (subTokens.size() == 1) {
        return subTokens.get(0).text;
    }
    StringBuilder sb = new StringBuilder("\"('");
    for (int i = 0; i < subTokens.size(); i++) {
        sb.append(subTokens.get(i).text);
        if (i < subTokens.size() - 1) {
            sb.append("', '");
        }
    }
    sb.append("')\"");
    return sb.toString();
}
// ---- helper method(s) introduced by the refactoring ----
private void addSubToken(String token, long startIndex) {
    subTokens.add(new SubToken(token, startIndex));
}

public static int calculateTotalSubTokens(List<Token> tokens) {
    int sum = 0;
    for (Token token : tokens) {
        sum += token.getNumSubTokens();
    }
    return sum;
}

