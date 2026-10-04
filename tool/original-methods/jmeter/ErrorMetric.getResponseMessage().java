/**
 * @return the response message, 'none' if the code is empty
 */
public String getResponseMessage() {
    if (responseMessage == null || responseMessage.isEmpty()) {
        return "None";
    } else {
        return responseMessage.trim();
    }
}