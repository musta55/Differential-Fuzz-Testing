@Override
public String generateCodeJava(FormatIdentifyer formatIdentifyer) {
    StringBuilder src = new StringBuilder();
    CodeGenTrie trie = new CodeGenTrie(properties, "dest.set", false, formatIdentifyer);
    String javaCode = trie.getJavaCode();
    appendVariableDeclarations(src);
    src.append("try { \n");
    if (properties.getRowIndexStructure().getProperties() == RowIndexStructure.IndexProperties.SeqScatter) {
        handleSeqScatterRowIndex(src, javaCode, formatIdentifyer);
    } else {
        handleDefaultRowIndex(src, javaCode);
    }
    src.append("} \n");
    src.append("catch(Exception ex){ \n");
    src.append("ex.printStackTrace(); \n");
    src.append("} \n");
    src.append("return row; \n");
    return javaTemplate.replace(code, src.toString());
}
// ---- helper method(s) introduced by the refactoring ----
private void appendVariableDeclarations(StringBuilder src) {
    src.append("String str=\"\"; \n");
    src.append("String[] parts; \n");
    src.append("String remainStr = \"\"; \n");
    src.append("int col = -1; \n");
    src.append("long lnnz = 0; \n");
    src.append("int index, indexConflict, endPos, strLen; \n");
    src.append("HashSet<String>[] endWithValueString = _props.endWithValueStrings(); \n");
}

private void handleSeqScatterRowIndex(StringBuilder src, String javaCode, FormatIdentifyer formatIdentifyer) {
    src.append("int rlen = splitInfo.getNrows();");
    src.append("int ri; \n");
    src.append("int endRow = row + rlen; \n");
    src.append("int beginPosStr, endPosStr; \n");
    src.append("StringBuilder sb = new StringBuilder(splitInfo.getRemainString()); \n");
    src.append("long beginIndex = splitInfo.getRecordIndexBegin(0); \n");
    src.append("long endIndex = splitInfo.getRecordIndexEnd(0); \n");
    src.append("boolean flag; \n");
    src.append("if(sb.length() > 0) { \n");
    src.append("ri = 0; \n");
    src.append("while(ri < beginIndex) { \n");
    src.append("reader.next(key, value); \n");
    src.append("sb.append(value.toString()); \n");
    src.append("ri++; \n");
    src.append("} \n");
    src.append("reader.next(key, value); \n");
    src.append("String valStr = value.toString(); \n");
    src.append("sb.append(valStr.substring(0, splitInfo.getRecordPositionBegin(0))); \n");
    src.append("str = sb.toString();");
    src.append("strLen = str.length(); \n");
    src.append(javaCode);
    src.append("sb = new StringBuilder(valStr.substring(splitInfo.getRecordPositionBegin(0))); \n");
    src.append("} \n");
    src.append("else { \n");
    src.append("ri = -1; \n");
    src.append("} \n");
    src.append("int rowCounter = 0; \n");
    src.append("while(row < endRow) { \n");
    src.append("flag = reader.next(key, value); \n");
    src.append("if(flag) { \n");
    src.append("ri++; \n");
    src.append("String valStr = value.toString(); \n");
    src.append("if(ri >= beginIndex && ri <= endIndex) { \n");
    src.append("beginPosStr = ri == beginIndex ? splitInfo.getRecordPositionBegin(rowCounter) : 0; \n");
    src.append("endPosStr = ri == endIndex ? splitInfo.getRecordPositionEnd(rowCounter) : valStr.length(); \n");
    src.append("sb.append(valStr.substring(beginPosStr, endPosStr)); \n");
    src.append("remainStr = valStr.substring(endPosStr); \n");
    src.append("continue; \n");
    src.append("} \n");
    src.append("else { \n");
    src.append("str = sb.toString(); \n");
    src.append("sb = new StringBuilder(); \n");
    src.append("sb.append(remainStr).append(valStr); \n");
    src.append("if(rowCounter + 1 < splitInfo.getListSize()) { \n");
    src.append("beginIndex = splitInfo.getRecordIndexBegin(rowCounter + 1); \n");
    src.append("endIndex = splitInfo.getRecordIndexEnd(rowCounter + 1); \n");
    src.append("} \n");
    src.append("rowCounter++; \n");
    src.append("} \n");
    src.append("} \n");
    src.append("else { \n");
    src.append("str = sb.toString(); \n");
    src.append("sb = new StringBuilder(); \n");
    src.append("} \n");
}

private void handleDefaultRowIndex(StringBuilder src, String javaCode) {
    src.append("while(reader.next(key, value)) { \n");
    src.append("str = value.toString(); \n");
    src.append("strLen = str.length(); \n");
    src.append(javaCode).append("\n");
}

