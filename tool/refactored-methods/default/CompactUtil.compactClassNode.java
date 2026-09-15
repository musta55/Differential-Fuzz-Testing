public static CompactClassNode compactClassNode(ClassNode cn) {
    if (cn == null) {
        return null;
    }
    CompactClassNode ccn = new CompactClassNode();
    ccn.setAccess(cn.access);
    ccn.setDefaultConstructor(compactMethodNode(ASMUtil.getPublicDefaultConstructor(cn)));
    ccn.setGetterMethods(compactMethods(ASMUtil.getPublicGetter(cn)));
    ccn.setSetterMethods(compactMethods(ASMUtil.getPublicSetter(cn)));
    ccn.setPorts(new LinkedList<CompactFieldNode>());
    ccn.setName(cn.name);
    ccn.setInnerClasses(compactInnerClasses(cn.innerClasses));
    if (ASMUtil.isEnum(cn)) {
        ccn.setEnumValues(ASMUtil.getEnumValues(cn));
    }
    if (cn instanceof ClassNodeType) {
        ccn.setCsv(((ClassNodeType) cn).csv);
    }
    return ccn;
}
// ---- helper method(s) introduced by the refactoring ----
private static List<CompactMethodNode> compactMethods(List<MethodNode> methods) {
    List<CompactMethodNode> cmns = new LinkedList<>();
    for (MethodNode mn : methods) {
        cmns.add(compactMethodNode(mn));
    }
    return cmns;
}

private static List<CompactFieldNode> compactFields(List<FieldNode> fields) {
    List<CompactFieldNode> ports = new LinkedList<>();
    for (FieldNode fn : fields) {
        ports.add(compactFieldNode(fn));
    }
    return ports;
}

private static List<CompactClassNode> compactInnerClasses(List<Object> innerClasses) {
    List<CompactClassNode> ccns = new LinkedList<>();
    for (Object icn : innerClasses) {
        InnerClassNode inner = (InnerClassNode) icn;
        CompactClassNode compactInner = new CompactClassNode();
        compactInner.setName(inner.name);
        compactInner.setAccess(inner.access);
        ccns.add(compactInner);
    }
    return ccns;
}

private static void setMethodSignatureNode(MethodNode mn, CompactMethodNode cmn) {
    if (mn instanceof com.datatorrent.stram.webapp.asm.MethodNode) {
        cmn.setMethodSignatureNode(((com.datatorrent.stram.webapp.asm.MethodNode) mn).signatureNode);
    }
}

private static void setFieldSignatureNode(FieldNode fn, CompactFieldNode cfn) {
    if (fn instanceof com.datatorrent.stram.webapp.asm.FieldNode) {
        cfn.setFieldSignatureNode(((com.datatorrent.stram.webapp.asm.FieldNode) fn).signatureNode);
    }
}

private static void setFieldAnnotations(FieldNode fn, CompactFieldNode cfn) {
    setAnnotationNode(fn, cfn);
}

private static String getClassName(String desc) {
    String className = org.apache.xbean.asm5.Type.getObjectType(desc).getClassName();
    if (className.startsWith("L")) {
        className = className.substring(1);
    }
    if (className.endsWith(";")) {
        className = className.substring(0, className.length() - 1);
    }
    return className;
}

private static boolean isPortAnnotation(AnnotationNode annotation) {
    return annotation.desc.contains("InputPortFieldAnnotation") || annotation.desc.contains("OutputPortFieldAnnotation");
}

private static Map<String, Object> extractAnnotationValues(AnnotationNode annotation) {
    Map<String, Object> annotationMap = new HashMap<>();
    List<Object> annotationValues = annotation.values;
    if (annotationValues != null) {
        for (int i = 0; i < annotationValues.size(); i += 2) {
            String key = (String) annotationValues.get(i);
            Object value = annotationValues.get(i + 1);
            annotationMap.put(key, value);
        }
    }
    return annotationMap;
}

