@Override
public SignatureVisitor visitParameterType() {
    return this;
}
// ---- helper method(s) introduced by the refactoring ----
private void clearVisitingStack() {
    visitingStack.clear();
}

private void handleSuperClass() {
    if (!visitingStack.isEmpty() && end == END.SUPERCLASS) {
        superClass = visitingStack.pop();
    }
}

private void addInterface() {
    if (interfaces == null) {
        interfaces = new LinkedList<>();
    }
    if (end == END.INTERFACE) {
        interfaces.add(0, visitingStack.pop());
    }
}

private void initializeInterfaces() {
    if (interfaces == null) {
        interfaces = new LinkedList<>();
    }
}

private void addLastInterface() {
    if (end == END.INTERFACE && !visitingStack.isEmpty()) {
        interfaces.add(0, visitingStack.pop());
    }
}

private void setSuperClassIfNull() {
    if (superClass == null && end == END.SUPERCLASS) {
        superClass = visitingStack.pop();
    }
}

