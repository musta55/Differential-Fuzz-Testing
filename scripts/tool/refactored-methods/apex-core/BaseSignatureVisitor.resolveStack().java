private void resolveStack() {
    if (visitingStack.size() <= 1) {
        return;
    }
    Type top = visitingStack.pop();
    Type peek = visitingStack.peek();
    if (peek instanceof ParameterizedTypeNode) {
        ((ParameterizedTypeNode) peek).actualTypeArguments.add(top);
    } else if (peek instanceof ArrayTypeNode) {
        ((ArrayTypeNode) peek).actualArrayType = top;
        resolveStack();
    } else if (peek instanceof WildcardTypeNode) {
        ((WildcardTypeNode) peek).bounds.add(top);
        resolveStack();
    } else if (peek instanceof TypeVariableNode) {
        ((TypeVariableNode) peek).bounds.add(top);
        resolveStack();
    } else {
        visitingStack.push(top);
    }
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

