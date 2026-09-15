@Override
public SignatureVisitor visitInterface() {
    // could be superclass before this
    if (!visitingStack.isEmpty() && end == END.SUPERCLASS) {
        superClass = visitingStack.pop();
    }
    // could be another interface before this
    if (interfaces == null) {
        interfaces = new LinkedList<>();
    }
    if (end == END.INTERFACE) {
        interfaces.add(0, visitingStack.pop());
    }
    end = END.INTERFACE;
    return this;
}