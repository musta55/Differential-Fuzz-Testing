private static StringBuilder getNamedFunctionStr(String name, AbstractDefaultAjaxBehavior b, CallbackParameter... extraParameters) {
    StringBuilder sb = new StringBuilder();
    sb.append("function ").append(name).append("(");
    boolean first = true;
    for (CallbackParameter curExtraParameter : extraParameters) {
        if (curExtraParameter.getFunctionParameterName() != null) {
            if (first) {
                first = false;
            } else {
                sb.append(',');
            }
            sb.append(curExtraParameter.getFunctionParameterName());
        }
    }
    sb.append(") {\n");
    sb.append(b.getCallbackFunctionBody(extraParameters));
    sb.append("}\n");
    return sb;
}