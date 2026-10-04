/**
 * Get transient inputs from either paramMap or transientWrites.
 * Inputs from paramMap has higher priority than inputs from transientWrites.
 * @param currentHop hop for which inputs are read from maps
 * @param paramMap of local parameters
 * @param transientWrites map of transient writes
 * @param localVariableMap map of local variables
 * @return inputs of currentHop
 */
public static ArrayList<Hop> getTransientInputs(Hop currentHop, Map<String, Hop> paramMap, Map<String, Hop> transientWrites, LocalVariableMap localVariableMap) {
    String hopName = currentHop.getName();
    Hop tWriteHop = paramMap != null ? paramMap.get(hopName) : null;
    if (tWriteHop == null) {
        tWriteHop = transientWrites.get(hopName);
        if (tWriteHop == null && localVariableMap.get(hopName) != null) {
            return null;
        }
    }
    if (tWriteHop == null) {
        throw new DMLRuntimeException("Transient write not found for " + currentHop);
    }
    return new ArrayList<>(Collections.singletonList(tWriteHop));
}