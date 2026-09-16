@Override
public String execute(SampleResult previousResult, Sampler currentSampler) throws InvalidVariableException {
    String digestAlgorithm = values[0].execute();
    String stringToEncode = values[1].execute();
    String salt = values.length > 2 ? values[2].execute() : null;
    String encodedString = null;
    try {
        MessageDigest md = MessageDigest.getInstance(digestAlgorithm);
        md.update(stringToEncode.getBytes(StandardCharsets.UTF_8));
        if (StringUtils.isNotEmpty(salt)) {
            md.update(salt.getBytes(StandardCharsets.UTF_8));
        }
        byte[] bytes = md.digest();
        encodedString = uppercase(Hex.encodeHexString(bytes), values, 3);
        addVariableValue(encodedString, values, 4);
    } catch (NoSuchAlgorithmException e) {
        log.error("Error calling {} function with value {}, digest algorithm {}, salt {}, ", KEY, stringToEncode, digestAlgorithm, salt, e);
    }
    return encodedString;
}