@Override
public SignatureVisitor visitTypeArgument(char typeArg) {
    TypeNode t = (TypeNode) visitingStack.pop();
    if (t instanceof ParameterizedTypeNode) {
        visitingStack.push(t);
    } else {
        ParameterizedTypeNode pt = new ParameterizedTypeNode();
        pt.setObjByteCode(t.getObjByteCode());
        visitingStack.push(pt);
    }
    if (typeArg == SignatureVisitor.INSTANCEOF) {
        return this;
    }
    WildcardTypeNode wtn = new WildcardTypeNode();
    wtn.boundChar = typeArg;
    visitingStack.push(wtn);
    return this;
}