@Override
public void visitTypeVariable(String typeVariable) {
    boolean found = false;
    for (TypeVariableNode typeVariableNode : typeV) {
        if (typeVariableNode.typeLiteral.equals(typeVariable)) {
            visitingStack.push(typeVariableNode);
            found = true;
            break;
        }
    }
    if (!found) {
        TypeNode tn = new TypeNode();
        tn.setObjByteCode("T" + typeVariable + ";");
        visitingStack.push(tn);
    }
    resolveStack();
}