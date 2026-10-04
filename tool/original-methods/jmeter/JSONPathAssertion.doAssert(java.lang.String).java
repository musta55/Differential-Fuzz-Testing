private void doAssert(String jsonString) {
    Object value = JsonPath.read(jsonString, getJsonPath());
    if (!isJsonValidationBool()) {
        if (value instanceof JSONArray) {
            JSONArray arrayValue = (JSONArray) value;
            if (arrayValue.isEmpty() && !JsonPath.isPathDefinite(getJsonPath())) {
                throw new IllegalStateException(String.format("JSONPath '%s' is indefinite and the extracted Value is an empty Array." + " Please use an assertion value, to be sure to get a correct result. Expected value was '%s'", getJsonPath(), getExpectedValue()));
            }
        }
        return;
    }
    if (value instanceof JSONArray) {
        if (arrayMatched((JSONArray) value)) {
            return;
        }
    } else {
        if ((isExpectNull() && value == null) || isEquals(value)) {
            return;
        }
    }
    if (isExpectNull()) {
        throw new IllegalStateException(String.format("Value in json path '%s' expected to be null, but found '%s'", getJsonPath(), value));
    } else {
        String msg;
        if (isUseRegex()) {
            msg = "Value in json path '%s' expected to match regexp '%s', but it did not match: '%s'";
        } else {
            msg = "Value in json path '%s' expected to be '%s', but found '%s'";
        }
        throw new IllegalStateException(String.format(msg, getJsonPath(), getExpectedValue(), objectToString(value)));
    }
}