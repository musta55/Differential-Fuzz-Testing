@Override
public String generateCodeJava(FormatIdentifyer formatIdentifyer) {
    StringBuilder src = new StringBuilder();
    CodeGenTrie trie = new CodeGenTrie(properties, "dest.appendValue", true, formatIdentifyer);
    appendInitialVariables(src);
    appendEndWithValueStrings(src);
    appendTryCatchBlock(src, formatIdentifyer);
    appendFinalStatements(src);
    return javaTemplate.replace(code, src.toString());
}
// ---- helper method(s) introduced by the refactoring ----
private void appendInitialVariables(StringBuilder src) {
    src.append("String str=\"\"; \n");
    src.append("String remainStr = \"\"; \n");
    src.append("int col = -1; \n");
    src.append("String[] parts; \n");
    src.append("int row = rowPos.intValue(); \n");
    src.append("long lnnz = 0; \n");
    src.append("int index, endPos, strLen; \n");
}

private void appendEndWithValueStrings(StringBuilder src) {
    boolean flag1 = false;
    boolean flag2 = false;
    if (properties.getRowIndexStructure().getProperties() == RowIndexStructure.IndexProperties.RowWiseExist || properties.getRowIndexStructure().getProperties() == RowIndexStructure.IndexProperties.CellWiseExist) {
        src.append("HashSet<String> endWithValueStringRow = _props.getRowIndexStructure().endWithValueStrings(); \n");
        flag1 = true;
    }
    if (properties.getColIndexStructure().getProperties() == ColIndexStructure.IndexProperties.CellWiseExist) {
        src.append("HashSet<String> endWithValueStringCol = _props.getColIndexStructure().endWithValueStrings(); \n");
        flag2 = true;
    }
    if (flag1 && flag2)
        src.append("HashSet<String> endWithValueStringVal = _props.endWithValueStrings()[0]; \n");
    else
        src.append("HashSet<String>[] endWithValueString = _props.endWithValueStrings(); \n");
}

private void appendTryCatchBlock(StringBuilder src, FormatIdentifyer formatIdentifyer) {
    src.append("try { \n");
    if (properties.getRowIndexStructure().getProperties() == RowIndexStructure.IndexProperties.SeqScatter) {
        appendSeqScatterLogic(src, formatIdentifyer);
    } else {
        src.append("while(reader.next(key, value)) { \n");
        src.append("str = value.toString(); \n");
    }
    src.append("strLen = str.length(); \n");
    src.append(new CodeGenTrie(properties, "dest.appendValue", true, formatIdentifyer).getJavaCode());
    src.append("} \n");
    src.append("} \n");
    src.append("catch(Exception ex){ \n");
    src.append("} \n");
}

private void appendSeqScatterLogic(StringBuilder src, FormatIdentifyer formatIdentifyer) {
    src.append("int ri = -1; \n");
    src.append("int beginPosStr, endPosStr; \n");
    src.append("StringBuilder sb = new StringBuilder(); \n");
    src.append("long beginIndex = splitInfo.getRecordIndexBegin(0); \n");
    src.append("long endIndex = splitInfo.getRecordIndexEnd(0); \n");
    src.append("boolean flag = true; \n");
    src.append("while(flag || sb.length() > 0) { \n");
    src.append("flag = reader.next(key, value); \n");
    src.append("if(flag) { \n");
    src.append("ri++; \n");
    src.append("String valStr = value.toString(); \n");
    src.append("beginPosStr = ri == beginIndex ? splitInfo.getRecordPositionBegin(row) : 0; \n");
    src.append("endPosStr = ri == endIndex ? splitInfo.getRecordPositionEnd(row): valStr.length(); \n");
    src.append("if(ri >= beginIndex && ri <= endIndex){ \n");
    src.append("sb.append(valStr.substring(beginPosStr, endPosStr)); \n");
    src.append("remainStr = valStr.substring(endPosStr); \n");
    src.append("continue; \n");
    src.append("} \n");
    src.append("else { \n");
    src.append("str = sb.toString(); \n");
    src.append("sb = new StringBuilder(); \n");
    src.append("sb.append(remainStr).append(valStr); \n");
    src.append("beginIndex = splitInfo.getRecordIndexBegin(row+1); \n");
    src.append("endIndex = splitInfo.getRecordIndexEnd(row+1); \n");
    src.append("} \n");
    src.append("} \n");
    src.append("else {\n");
    src.append("str = sb.toString(); \n");
    src.append("sb = new StringBuilder();\n");
    src.append("}");
}

private void appendFinalStatements(StringBuilder src) {
    src.append("rowPos.setValue(row); \n");
    src.append("return lnnz; \n");
}

