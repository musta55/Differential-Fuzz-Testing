@Override
public BaseFileItem.Type unmarshal(String v) throws Exception {
    if (Strings.isEmpty(v)) {
        return null;
    }
    String upperCaseValue = v.toUpperCase(Locale.ROOT);
    return typeMap.getOrDefault(upperCaseValue, BaseFileItem.Type.valueOf(upperCaseValue));
}