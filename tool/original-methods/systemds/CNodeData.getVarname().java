@Override
public String getVarname() {
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
}