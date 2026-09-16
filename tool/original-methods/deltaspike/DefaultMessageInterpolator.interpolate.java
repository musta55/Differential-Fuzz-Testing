@Override
public String interpolate(String messageTemplate, Serializable[] arguments, Locale locale) {
    if (arguments == null || arguments.length == 0) {
        return messageTemplate;
    }
    return String.format(locale, messageTemplate, arguments);
}