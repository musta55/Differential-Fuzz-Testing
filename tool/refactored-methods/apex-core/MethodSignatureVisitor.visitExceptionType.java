@Override
public SignatureVisitor visitExceptionType() {
    if (!visitingStack.isEmpty()) {
        if (stage == VISIT_RETURN) {
            returnType = visitingStack.pop();
        } else if (stage == VISIT_EXCEPTION) {
            exceptionType.add(visitingStack.pop());
        }
    }
    stage = VISIT_EXCEPTION;
    return this;
}