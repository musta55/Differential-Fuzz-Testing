/**
 * Return parameter map containing the mapping from parameter name to input hop
 * for all parameters of the function hop.
 * @param funcOp hop for which the mapping of parameter names to input hops are made
 * @return parameter map or empty map if function has no parameters
 */
public static Map<String, Hop> getParamMap(FunctionOp funcOp) {
    String[] inputNames = funcOp.getInputVariableNames();
    Map<String, Hop> paramMap = new HashMap<>();
    if (inputNames != null) {
        for (int i = 0; i < funcOp.getInput().size(); i++) paramMap.put(inputNames[i], funcOp.getInput(i));
    }
    return paramMap;
}