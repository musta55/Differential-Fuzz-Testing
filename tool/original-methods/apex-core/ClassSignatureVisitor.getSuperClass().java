public Type getSuperClass() {
    if (superClass == null && end == END.SUPERCLASS) {
        superClass = visitingStack.pop();
    }
    return superClass;
}