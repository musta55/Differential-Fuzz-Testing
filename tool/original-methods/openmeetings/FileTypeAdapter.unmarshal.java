@Override
public BaseFileItem.Type unmarshal(String v) throws Exception {
    if ("PollChart".equalsIgnoreCase(v)) {
        return BaseFileItem.Type.POLL_CHART;
    }
    if ("WmlFile".equalsIgnoreCase(v)) {
        return BaseFileItem.Type.WML_FILE;
    }
    return Strings.isEmpty(v) ? null : BaseFileItem.Type.valueOf(v.toUpperCase(Locale.ROOT));
}