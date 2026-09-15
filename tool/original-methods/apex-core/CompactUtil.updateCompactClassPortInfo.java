public static void updateCompactClassPortInfo(ClassNode cn, CompactClassNode ccn) {
    List<FieldNode> fields = ASMUtil.getPorts(cn);
    List<CompactFieldNode> ports = new LinkedList<>();
    for (FieldNode fn : fields) {
        ports.add(compactFieldNode(fn));
    }
    ccn.setPorts(ports);
}