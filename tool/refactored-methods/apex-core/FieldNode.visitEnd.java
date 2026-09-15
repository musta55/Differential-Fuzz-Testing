@Override
public void visitEnd() {
    super.visitEnd();
    parseSignature(signature != null ? signature : desc);
}
// ---- helper method(s) introduced by the refactoring ----
private void parseSignature(String methodString) {
    SignatureReader reader = new SignatureReader(methodString);
    signatureNode = new FieldSignatureVisitor();
    signatureNode.typeV.addAll(typeVariableSignatureNode.typeV);
    reader.accept(signatureNode);
}

