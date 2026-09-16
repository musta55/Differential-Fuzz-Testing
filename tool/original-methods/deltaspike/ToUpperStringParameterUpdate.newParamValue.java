@Override
public Object newParamValue(Object current) {
    if (current instanceof String) {
        return ((String) current).toUpperCase();
    }
    return current;
}