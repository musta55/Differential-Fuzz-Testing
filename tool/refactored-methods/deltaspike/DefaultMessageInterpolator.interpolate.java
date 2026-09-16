@Override
public String interpolate(String messageTemplate, Serializable[] arguments, Locale locale) {
    if (messageTemplate == null) {
        throw new IllegalArgumentException("messageTemplate cannot be null");
    }
    if (arguments == null || arguments.length == 0) {
        return messageTemplate;
    }
    try {
        return String.format(locale, messageTemplate, arguments);
    } catch (Exception e) {
        throw new RuntimeException("Error formatting message", e);
    }
}