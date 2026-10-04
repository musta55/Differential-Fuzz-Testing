/**
 * @return the response code , '0' if the code is empty
 */
public String getResponseCode() {
    return responseCode.isEmpty() ? "0" : responseCode.trim();
}