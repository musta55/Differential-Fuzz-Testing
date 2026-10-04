public static boolean isValidParamName(String key) {
    for (String paramName : WRITE_VALID_PARAM_NAMES) if (paramName.equals(key))
        return true;
    return false;
}