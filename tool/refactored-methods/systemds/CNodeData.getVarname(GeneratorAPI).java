public String getVarname(GeneratorAPI api) {
    return getVarnameInternal(_name, api);
}
// ---- helper method(s) introduced by the refactoring ----
private String getVarnameInternal(String name, GeneratorAPI api) {
    if ("NaN".equals(name))
        return "Double.NaN";
    else if ("Infinity".equals(name))
        return "Double.POSITIVE_INFINITY";
    else if ("-Infinity".equals(name))
        return "Double.NEGATIVE_INFINITY";
    else if ("true".equals(name) || "false".equals(name))
        return "true".equals(name) ? "1d" : "0d";
    else if (api == GeneratorAPI.CUDA) {
        if ("NaN".equals(name))
            return isSinglePrecision() ? "CUDART_NAN_F" : "CUDART_NAN";
        else if ("Infinity".equals(name))
            return isSinglePrecision() ? "CUDART_INF_F" : "CUDART_INF";
        else if ("-Infinity".equals(name))
            return isSinglePrecision() ? "-CUDART_INF_F" : "-CUDART_INF";
        else if ("true".equals(name) || "false".equals(name))
            return "true".equals(name) ? "1" : "0";
        else if (CodegenUtils.isNumeric(name))
            return isSinglePrecision() ? name + ".0f" : name + ".0";
    }
    return name;
}

