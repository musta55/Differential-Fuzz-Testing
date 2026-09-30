@Override
public void visitClassType(String classType) {
    TypeNode tn = new TypeNode();
    tn.setObjByteCode("L" + classType + ";");
    visitingStack.push(tn);
}
// ---- helper method(s) introduced by the refactoring ----
private ParameterizedTypeNode ensureParameterizedType(TypeNode t) {
    if (t instanceof ParameterizedTypeNode) {
        return (ParameterizedTypeNode) t;
    } else {
        ParameterizedTypeNode pt = new ParameterizedTypeNode();
        pt.setObjByteCode(t.getObjByteCode());
        return pt;
    }
}

private TypeVariableNode findTypeVariableNode(String typeVariable) {
    for (TypeVariableNode typeVariableNode : typeV) {
        if (typeVariableNode.typeLiteral.equals(typeVariable)) {
            return typeVariableNode;
        }
    }
    return null;
}

private TypeVariableNode createAndAddTypeVariableNode(String typeVariable) {
    TypeNode tn = new TypeNode();
    tn.setObjByteCode("T" + typeVariable + ";");
    TypeVariableNode tvn = new TypeVariableNode();
    tvn.typeLiteral = typeVariable;
    tvn.bounds.add(tn);
    typeV.add(tvn);
    return tvn;
}

