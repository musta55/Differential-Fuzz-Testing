@Override
public String toString() {
    if (subTokens.size() == 0) {
        return EMPTY_TOKEN;
    }
    if (subTokens.size() == 1) {
        return subTokens.get(0).text;
    }
    StringBuilder sb = new StringBuilder().append("\"('");
    for (int i = 0; i < subTokens.size(); i++) {
        sb.append(subTokens.get(i).text);
        if (i < subTokens.size() - 1)
            sb.append("', '");
    }
    sb.append("')\"");
    //return "\"('" + subTokens.stream().map(subToken -> subToken.text).collect(Collectors.joining("', '")) + "')\"";
    return sb.toString();
}