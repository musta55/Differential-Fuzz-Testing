private void resolveStack() {
    if (visitingStack.isEmpty() || visitingStack.size() == 1) {
        return;
    }
    Type top = visitingStack.pop();
    Type peek = visitingStack.peek();
    if (peek instanceof ParameterizedTypeNode) {
        ((ParameterizedTypeNode) peek).actualTypeArguments.add(top);
        return;
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
        return;
    }
}