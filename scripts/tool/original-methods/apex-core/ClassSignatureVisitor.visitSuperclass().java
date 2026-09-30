@Override
public SignatureVisitor visitSuperclass() {
    visitingStack.clear();
    end = END.SUPERCLASS;
    return this;
}