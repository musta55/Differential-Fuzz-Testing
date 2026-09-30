@Override
public void visitClassType(String classType) {
    Type.TypeNode tn = new Type.TypeNode();
    tn.setObjByteCode("L" + classType + ";");
    visitingStack.push(tn);
    // base type could only appear in method parameter list or return type
    //    if(stage == VISIT_PARAM) {
    //      visitingStack.push(tn);
    //    }
    //    if(stage == VISIT_RETURN) {
    //      returnType = tn;
    //    } if(stage == VISIT_EXCEPTION) {
    //      exceptionType = tn;
    //    }
}