public Type getFieldType() {
    if (!visitingStack.isEmpty()) {
        fieldType = visitingStack.pop();
        visitingStack.push(fieldType);
        return fieldType;
    } else {
        return null;
    }
}