public List<Type> getInterfaces() {
    if (interfaces == null) {
        interfaces = new LinkedList<>();
    }
    if (interfaces != null && end == END.INTERFACE && !visitingStack.isEmpty()) {
        interfaces.add(0, visitingStack.pop());
    }
    return interfaces;
}