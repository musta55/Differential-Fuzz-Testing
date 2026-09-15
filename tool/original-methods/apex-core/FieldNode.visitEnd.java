@Override
public void visitEnd() {
    super.visitEnd();
    String methodString = signature != null ? signature : desc;
    SignatureReader reader = new SignatureReader(methodString);
    signatureNode = new FieldSignatureVisitor();
    signatureNode.typeV.addAll(typeVariableSignatureNode.typeV);
    reader.accept(signatureNode);
}