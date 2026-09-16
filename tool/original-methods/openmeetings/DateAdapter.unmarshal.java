@Override
public Date unmarshal(String v) throws Exception {
    if (v == null || "null".equals(v)) {
        return null;
    }
    try {
        Long t = Long.valueOf(v);
        if (t != null) {
            return new Date(t);
        }
    } catch (Exception err) {
        //no-op
    }
    return null;
}