@Override
public SignatureVisitor visitParameterType() {
    if (stage == VISIT_FORMAL_TYPE) {
        stage = VISIT_PARAM;
        if (!visitingStack.isEmpty()) {
            visitingStack.pop();
        }
        return this;
    }
    stage = VISIT_PARAM;
    if (!visitingStack.isEmpty()) {
        parameters.add(visitingStack.pop());
    }
    return this;
}