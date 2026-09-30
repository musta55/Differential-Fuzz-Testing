private static CompactMethodNode compactMethodNode(MethodNode mn) {
    if (mn == null) {
        return null;
    }
    CompactMethodNode cmn = new CompactMethodNode();
    cmn.setName(mn.name);
    if (mn instanceof com.datatorrent.stram.webapp.asm.MethodNode) {
        cmn.setMethodSignatureNode(((com.datatorrent.stram.webapp.asm.MethodNode) mn).signatureNode);
    }
    return cmn;
}