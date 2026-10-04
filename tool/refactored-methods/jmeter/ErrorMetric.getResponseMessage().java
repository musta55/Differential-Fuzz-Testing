/**
 * @return the response message, 'none' if the code is empty
 */
public String getResponseMessage() {
    return StringUtils.defaultIfEmpty(responseMessage, "None").trim();
}