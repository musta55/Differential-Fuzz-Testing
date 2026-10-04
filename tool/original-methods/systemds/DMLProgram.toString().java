@Override
public String toString() {
    StringBuilder sb = new StringBuilder();
    // for each namespace, display all functions
    for (String namespaceKey : this.getNamespaces().keySet()) {
        sb.append("NAMESPACE = " + namespaceKey + "\n");
        FunctionDictionary<FunctionStatementBlock> dict = getNamespaces().get(namespaceKey);
        sb.append("FUNCTIONS = ");
        for (FunctionStatementBlock fsb : dict.getFunctions().values()) {
            sb.append(fsb);
            sb.append(", ");
        }
        sb.append("\n");
        sb.append("********************************** \n");
    }
    sb.append("******** MAIN SCRIPT BODY ******** \n");
    for (StatementBlock b : _blocks) {
        sb.append(b);
        sb.append("\n");
    }
    sb.append("********************************** \n");
    return sb.toString();
}