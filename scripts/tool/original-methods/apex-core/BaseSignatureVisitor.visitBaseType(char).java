@Override
public void visitBaseType(char baseType) {
    Type.TypeNode tn = new Type.TypeNode();
    tn.setObjByteCode(baseType + "");
    visitingStack.push(tn);
    resolveStack();
    // base type could only appear in method parameter list or return type
    //    if(stage == VISIT_PARAM) {
    //      visitingStack.push(tn);
    //    }
    //    if(stage == VISIT_RETURN) {
    //      returnType = tn;
    //    }
}