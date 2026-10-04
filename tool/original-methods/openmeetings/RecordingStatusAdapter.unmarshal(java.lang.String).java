@Override
public Recording.Status unmarshal(String v) throws Exception {
    if ("PROCESSING".equalsIgnoreCase(v)) {
        return Recording.Status.CONVERTING;
    }
    Recording.Status result = Status.NONE;
    if (!Strings.isEmpty(v)) {
        try {
            result = Recording.Status.valueOf(v);
        } catch (Exception e) {
            //no-op
        }
    }
    return result;
}