public Type getFieldType() {
    if (!visitingStack.isEmpty()) {
        return visitingStack.pop();
    } else {
        return null;
    }
}