@Override
public String execute(SampleResult previousResult, Sampler currentSampler) throws InvalidVariableException {
    String digestAlgorithm = values[0].execute();
    String stringToEncode = values[1].execute();
    String salt = getSaltValue();
    String encodedString = computeDigest(digestAlgorithm, stringToEncode, salt);
    encodedString = uppercase(encodedString, values, 3);
    addVariableValue(encodedString, values, 4);
    return encodedString;
}
// ---- helper method(s) introduced by the refactoring ----
private String getSaltValue() {
    return values.length > 2 ? values[2].execute() : null;
}

private static String computeDigest(String digestAlgorithm, String stringToEncode, String salt) {
    try {
        MessageDigest md = MessageDigest.getInstance(digestAlgorithm);
        md.update(stringToEncode.getBytes(StandardCharsets.UTF_8));
        if (StringUtils.isNotEmpty(salt)) {
            md.update(salt.getBytes(StandardCharsets.UTF_8));
        }
        byte[] bytes = md.digest();
        return Hex.encodeHexString(bytes);
    } catch (NoSuchAlgorithmException e) {
        log.error("Error calling {} function with value {}, digest algorithm {}, salt {}", KEY, stringToEncode, digestAlgorithm, salt, e);
        return null;
    }
}

