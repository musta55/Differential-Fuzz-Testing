@Override
public SignatureVisitor visitExceptionType() {
    if (stage == VISIT_RETURN && !visitingStack.isEmpty()) {
        returnType = visitingStack.pop();
    }
    if (stage == VISIT_EXCEPTION && !visitingStack.isEmpty()) {
        exceptionType.add(visitingStack.pop());
    }
    stage = VISIT_EXCEPTION;
    return this;
}