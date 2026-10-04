public String getVarname(GeneratorAPI api) {
    if (api == GeneratorAPI.JAVA) {
        if ("NaN".equals(_name))
            return "Double.NaN";
        else if ("Infinity".equals(_name))
            return "Double.POSITIVE_INFINITY";
        else if ("-Infinity".equals(_name))
            return "Double.NEGATIVE_INFINITY";
        else if ("true".equals(_name) || "false".equals(_name))
            return "true".equals(_name) ? "1d" : "0d";
        else
            return _name;
    } else if (api == GeneratorAPI.CUDA) {
        if ("NaN".equals(_name))
            return isSinglePrecision() ? "CUDART_NAN_F" : "CUDART_NAN";
        else if ("Infinity".equals(_name))
            return isSinglePrecision() ? "CUDART_INF_F" : "CUDART_INF";
        else if ("-Infinity".equals(_name))
            return isSinglePrecision() ? "-CUDART_INF_F" : "-CUDART_INF";
        else if ("true".equals(_name) || "false".equals(_name))
            return "true".equals(_name) ? "1" : "0";
        else if (CodegenUtils.isNumeric(_name))
            return isSinglePrecision() ? _name + ".0f" : _name + ".0";
        else
            return _name;
    } else
        throw new RuntimeException("Unknown GeneratorAPI: " + SpoofCompiler.API);
}