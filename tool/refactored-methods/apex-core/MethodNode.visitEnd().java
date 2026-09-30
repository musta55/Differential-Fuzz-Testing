@Override
public void visitEnd() {
    super.visitEnd();
    String methodString = getMethodSignature();
    SignatureReader reader = new SignatureReader(methodString);
    signatureNode = new MethodSignatureVisitor();
    signatureNode.typeV.addAll(typeVariableSignatureNode.typeV);
    reader.accept(signatureNode);
}
// ---- helper method(s) introduced by the refactoring ----
private String getMethodSignature() {
    return signature != null ? signature : desc;
}

