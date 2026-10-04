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
    Hop tWriteHop = null;
    if (paramMap != null)
        tWriteHop = paramMap.get(currentHop.getName());
    if (tWriteHop == null)
        tWriteHop = transientWrites.get(currentHop.getName());
    if (tWriteHop == null) {
        if (localVariableMap.get(currentHop.getName()) != null)
            return null;
        else
            throw new DMLRuntimeException("Transient write not found for " + currentHop);
    } else
        return new ArrayList<>(Collections.singletonList(tWriteHop));
}