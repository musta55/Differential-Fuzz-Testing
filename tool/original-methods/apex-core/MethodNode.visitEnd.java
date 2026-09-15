@Override
public void visitEnd() {
    super.visitEnd();
    String methodString = signature != null ? signature : desc;
    SignatureReader reader = new SignatureReader(methodString);
    signatureNode = new MethodSignatureVisitor();
    //    signatureNode.signature = methodString;
    signatureNode.typeV.addAll(typeVariableSignatureNode.typeV);
    reader.accept(signatureNode);
}