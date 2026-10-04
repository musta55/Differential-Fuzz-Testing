@Override
public Date unmarshal(String v) throws Exception {
    if ("null".equals(v)) {
        return null;
    }
    try {
        return new Date(Long.parseLong(v));
    } catch (NumberFormatException err) {
        //no-op
    }
    return null;
}