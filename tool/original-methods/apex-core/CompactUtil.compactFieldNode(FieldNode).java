private static CompactFieldNode compactFieldNode(FieldNode fn) {
    if (fn == null) {
        return null;
    }
    CompactFieldNode cfn = new CompactFieldNode();
    cfn.setName(fn.name);
    String className = org.apache.xbean.asm5.Type.getObjectType(fn.desc).getClassName();
    if (className.charAt(0) == 'L') {
        className = className.substring(1);
    }
    if (className.endsWith(";")) {
        className = className.substring(0, className.length() - 1);
    }
    cfn.setDescription(className);
    cfn.setSignature(fn.signature);
    if (fn.visibleAnnotations != null) {
        setAnnotationNode(fn, cfn);
    }
    if (fn instanceof com.datatorrent.stram.webapp.asm.FieldNode) {
        cfn.setFieldSignatureNode((((com.datatorrent.stram.webapp.asm.FieldNode) fn).signatureNode));
    }
    return cfn;
}