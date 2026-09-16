/**
 * @see DefaultAuthenticationStrategy#decode(String value)
 * Additionally decodes stored login type and domain
 */
@Override
protected String[] decode(String value) {
    if (!Strings.isEmpty(value)) {
        String username = null;
        String password = null;
        String type = null;
        String domainId = null;
        String[] values = value.split(VALUE_SEPARATOR);
        if (values.length > 0 && !Strings.isEmpty(values[0])) {
            username = values[0];
        }
        if (values.length > 1 && !Strings.isEmpty(values[1])) {
            password = values[1];
        }
        if (values.length > 2 && !Strings.isEmpty(values[2])) {
            type = values[2];
        }
        if (values.length > 3 && !Strings.isEmpty(values[3])) {
            domainId = values[3];
        }
        return new String[] { username, password, type, domainId };
    }
    return new String[] {};
}