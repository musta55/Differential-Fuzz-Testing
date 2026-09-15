@Override
public SignatureVisitor visitParameterType() {
    if (stage == VISIT_FORMAL_TYPE && !visitingStack.isEmpty()) {
        visitingStack.pop();
    }
    stage = VISIT_PARAM;
    if (!visitingStack.isEmpty()) {
        parameters.add(visitingStack.pop());
    }
    return this;
}