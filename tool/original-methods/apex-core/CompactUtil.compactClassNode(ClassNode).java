public static CompactClassNode compactClassNode(ClassNode cn) {
    if (cn == null) {
        return null;
    }
    CompactClassNode ccn = new CompactClassNode();
    ccn.setAccess(cn.access);
    ccn.setDefaultConstructor(compactMethodNode(ASMUtil.getPublicDefaultConstructor(cn)));
    List<CompactMethodNode> cmns = new LinkedList<>();
    for (MethodNode mn : ASMUtil.getPublicGetter(cn)) {
        cmns.add(compactMethodNode(mn));
    }
    ccn.setGetterMethods(cmns);
    cmns = new LinkedList<>();
    for (MethodNode mn : ASMUtil.getPublicSetter(cn)) {
        cmns.add(compactMethodNode(mn));
    }
    ccn.setSetterMethods(cmns);
    ccn.setPorts(new LinkedList<CompactFieldNode>());
    ccn.setName(cn.name);
    List<CompactClassNode> ccns = new LinkedList<>();
    for (Object icn : cn.innerClasses) {
        CompactClassNode inner = new CompactClassNode();
        inner.setName(((InnerClassNode) icn).name);
        inner.setAccess(((InnerClassNode) icn).access);
    }
    ccn.setInnerClasses(ccns);
    if (ASMUtil.isEnum(cn)) {
        ccn.setEnumValues(ASMUtil.getEnumValues(cn));
    }
    if (cn instanceof ClassNodeType) {
        ccn.setCsv(((ClassNodeType) cn).csv);
    }
    //    if(!CollectionUtils.isEmpty(cn.innerClasses)){
    //      ccn.setInnerClasses(Lists.transform(cn.innerClasses, new Function<InnerClassNode, CompactClassNode>(){
    //
    //        @Override
    //        public CompactClassNode apply(InnerClassNode input)
    //        {
    //          input.
    //          return null;
    //        }
    //
    //      }));
    //    }
    return ccn;
}