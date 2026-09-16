@Override
public void onException(ParseException ex) {
    final String message = "Failed to parse CSS: " + baseUrl + ", " + LoggingCSSParseErrorHandler.createLoggingStringParseError(ex);
    if (IGNORE_UNRECOVERABLE_PARSING_ERROR) {
        LOG.warn(message);
    } else {
        throw new IllegalArgumentException("Unrecoverable error met during parsing, " + "you can ignore such errors by setting property:" + "'httpsampler.ignore_failed_embedded_resource' to true, message:" + message);
    }
}