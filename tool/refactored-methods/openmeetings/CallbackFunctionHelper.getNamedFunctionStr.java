private static StringBuilder getNamedFunctionStr(String name, AbstractDefaultAjaxBehavior b, CallbackParameter... extraParameters) {
    StringBuilder sb = new StringBuilder();
    sb.append("function ").append(name).append("(");
    appendParameters(sb, extraParameters);
    sb.append(") {\n");
    sb.append(b.getCallbackFunctionBody(extraParameters));
    sb.append("}\n");
    return sb;
}
// ---- helper method(s) introduced by the refactoring ----
private static void appendParameters(StringBuilder sb, CallbackParameter... extraParameters) {
    boolean first = true;
    for (CallbackParameter curExtraParameter : extraParameters) {
        if (curExtraParameter.getFunctionParameterName() != null) {
            if (!first) {
                sb.append(',');
            } else {
                first = false;
            }
            sb.append(curExtraParameter.getFunctionParameterName());
        }
    }
}

