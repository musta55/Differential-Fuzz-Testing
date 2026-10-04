private void getJavaCodeIndexOf(CodeGenTrieNode node, StringBuilder src, String currPos, boolean arrayCodeGenEnable) {
    CodeGenTrieNode tmpNode = null;
    if (arrayCodeGenEnable)
        tmpNode = getJavaCodeRegular(node, src, currPos);
    if (tmpNode == null) {
        if (node.isEndOfCondition())
            src.append(node.geValueCode(destination, currPos));
        if (node.getChildren().size() > 0) {
            String currPosVariable = currPos;
            for (String key : node.getChildren().keySet()) {
                if (key.length() > 0) {
                    currPosVariable = getRandomName("curPos");
                    String mKey = key.replace("\\\"", Lop.OPERAND_DELIMITOR);
                    mKey = mKey.replace("\\", "\\\\");
                    mKey = mKey.replace(Lop.OPERAND_DELIMITOR, "\\\"");
                    if (node.getKey() == null) {
                        src.append("index = str.indexOf(\"" + mKey.replace("\\\"", "\"").replace("\"", "\\\"") + "\"); \n");
                    } else
                        src.append("index = str.indexOf(\"" + mKey.replace("\\\"", "\"").replace("\"", "\\\"") + "\", " + currPos + "); \n");
                    src.append("if(index != -1) { \n");
                    src.append("int " + currPosVariable + " = index + " + key.length() + "; \n");
                }
                CodeGenTrieNode child = node.getChildren().get(key);
                getJavaCodeIndexOf(child, src, currPosVariable, arrayCodeGenEnable);
                if (key.length() > 0)
                    src.append("} \n");
            }
        }
    } else if (!tmpNode.isEndOfCondition())
        getJavaCodeIndexOf(tmpNode, src, currPos, arrayCodeGenEnable);
}